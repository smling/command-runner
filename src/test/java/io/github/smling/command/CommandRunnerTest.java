package io.github.smling.command;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Duration;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CommandRunnerTest {

    @ParameterizedTest(name = "{0}")
    @MethodSource({
            "runScenariosForWindows",
            "runScenariosForLinux"
    })
    void run_coversAllBranches(String scenarioName,
                               String command,
                               List<String> args,
                               Duration timeout,
                               Class<? extends Throwable> expectedException,
                               Integer expectedExitCode,
                               String expectedOutputContains,
                               String expectedMessageContains) {
        CommandRunner runner = CommandRunner
                .builder()
                .duration(timeout)
                .build();

        if (expectedException == null) {
            ExecuteResult result = runner.run(command, args);
            assertNotNull(result);
            if (expectedExitCode != null) {
                assertEquals(expectedExitCode.intValue(), result.exitCode());
            }
            if (expectedOutputContains != null) {
                assertTrue(result.result().contains(expectedOutputContains));
            }
        } else {
            Throwable thrown = assertThrows(expectedException, () -> runner.run(command, args));
            if (expectedMessageContains != null) {
                assertTrue(thrown.getMessage().contains(expectedMessageContains));
            }
        }
    }

    static Stream<Arguments> runScenariosForLinux() {
        if (isWindows()) {
            return Stream.of();
        }
        return Stream.of(
                Arguments.of(
                        "success echo",
                        "sh",
                        List.of("-c", "echo hello"),
                        Duration.ofSeconds(5),
                        null,
                        0,
                        "hello",
                        null
                ),
                Arguments.of(
                        "non-zero exit code",
                        "sh",
                        List.of("-c", "exit 1"),
                        Duration.ofSeconds(5),
                        null,
                        1,
                        null,
                        null
                ),
                Arguments.of(
                        "timeout",
                        "sh",
                        List.of("-c", "sleep 5"),
                        Duration.ofMillis(500),
                        ExecuteCommandException.class,
                        null,
                        null,
                        "Command timed out"
                ),
                Arguments.of(
                        "start failure",
                        "definitely-not-a-real-command-12345",
                        List.of(),
                        Duration.ofSeconds(5),
                        ExecuteCommandException.class,
                        null,
                        null,
                        "Check command and argument correctness."
                )
        );
    }

    static Stream<Arguments> runScenariosForWindows() {
        if (!isWindows()) {
            return Stream.of();
        }
        return Stream.of(
                Arguments.of(
                        "success echo",
                        "cmd",
                        List.of("/c", "echo", "hello"),
                        Duration.ofSeconds(5),
                        null,
                        0,
                        "hello",
                        null
                ),
                Arguments.of(
                        "non-zero exit code",
                        "cmd",
                        List.of("/c", "exit", "1"),
                        Duration.ofSeconds(5),
                        null,
                        1,
                        null,
                        null
                ),
                Arguments.of(
                        "timeout",
                        "ping",
                        List.of("127.0.0.1", "-n", "6"),
                        Duration.ofMillis(500),
                        ExecuteCommandException.class,
                        null,
                        null,
                        "Command timed out"
                ),
                Arguments.of(
                        "start failure",
                        "definitely-not-a-real-command-12345",
                        List.of(),
                        Duration.ofSeconds(5),
                        ExecuteCommandException.class,
                        null,
                        null,
                        "Check command and argument correctness."
                )
        );
    }

    private static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }
}
