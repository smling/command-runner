package io.github.smling.command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Executes external system commands with a configurable timeout.
 * <p>
 * Instances are created via the {@link #builder()} factory method.
 */
public class CommandRunner {

    /**
     * Creates a new {@link CommandRunnerBuilder} for configuring a {@link CommandRunner}.
     *
     * @return a new builder instance
     */
    public static CommandRunnerBuilder builder() {
        return new CommandRunnerBuilder();
    }

    private final Duration timeout;

    private CommandRunner(Duration timeout) {
        this.timeout = timeout;
    }

    /**
     * Fluent builder for {@link CommandRunner} instances.
     * <p>
     * The default timeout is {@code 5} seconds.
     */
    public static class CommandRunnerBuilder {
        private Duration duration = Duration.ofSeconds(5);

        /**
         * Sets the maximum time to wait for the command to finish.
         *
         * @param duration timeout duration; must be positive
         * @return this builder for chaining
         */
        public CommandRunnerBuilder duration(Duration duration) {
            this.duration = duration;
            return this;
        }

        /**
         * Builds a new {@link CommandRunner} with the configured timeout.
         *
         * @return a configured {@link CommandRunner}
         */
        public CommandRunner build() {
            return new CommandRunner(duration);
        }
    }

    /**
     * Executes the given command with the provided arguments.
     * <p>
     * The process output (stdout and stderr combined) is captured and returned
     * together with the process exit code. If the process does not finish within
     * the configured timeout, it is forcibly terminated and an
     * {@link ExecuteCommandException} is thrown.
     *
     * @param command executable to run, for example {@code "sh"} or {@code "cmd"}
     * @param args    list of arguments passed to the executable
     * @return an {@link ExecuteResult} containing exit code and captured output
     * @throws ExecuteCommandException if the command cannot be started, times out or is interrupted
     */
    public ExecuteResult run(String command, List<String> args) {
        List<String> commandWithArgs = new ArrayList<>();
        commandWithArgs.add(command);
        commandWithArgs.addAll(args);

        ProcessBuilder pb = new ProcessBuilder(commandWithArgs);
        pb.redirectErrorStream(true);

        try {
            Process process = pb.start();
            boolean finished = process.waitFor(timeout.toMillis(), TimeUnit.MILLISECONDS);
            if (!finished) {
                process.destroyForcibly();
                throw new ExecuteCommandException("Command timed out: " + String.join(" ", command));
            }

            StringBuilder output = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append(System.lineSeparator());
                }
            }

            return new ExecuteResult(
                    process.exitValue(),
                    output.toString()
            );
        } catch (InterruptedException | IOException e) {
            throw new ExecuteCommandException("Error occurred when run command" + command + " " + args + ". Check command and argument correctness.", e);
        }
    }
}
