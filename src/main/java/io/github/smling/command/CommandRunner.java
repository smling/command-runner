package io.github.smling.command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CommandRunner {
    public static CommandRunnerBuilder builder() {
        return new CommandRunnerBuilder();
    }

    private final Duration timeout;

    private CommandRunner(Duration timeout) {
        this.timeout = timeout;
    }

    public static class CommandRunnerBuilder {
        private Duration duration = Duration.ofSeconds(5);

        public CommandRunnerBuilder duration(Duration duration) {
            this.duration = duration;
            return this;
        }

        public CommandRunner build() {
            return new CommandRunner(duration);
        }
    }

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
