package io.github.smling.command;

public class ExecuteCommandException extends RuntimeException {
    public ExecuteCommandException(String message) {
        super(message);
    }
    public ExecuteCommandException(String message, Exception exception) {
        super(message, exception);
    }
}
