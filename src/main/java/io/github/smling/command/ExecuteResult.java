package io.github.smling.command;

public record ExecuteResult(
        int exitCode,
        String result
) { }
