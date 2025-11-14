package io.github.smling.command;

/**
 * Result of executing an external command.
 *
 * @param exitCode numeric exit code returned by the process
 * @param result   combined standard output and error output of the process
 */
public record ExecuteResult(
        int exitCode,
        String result
) { }
