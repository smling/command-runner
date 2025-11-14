package io.github.smling.command;

/**
 * Unchecked exception thrown when executing an external command fails.
 * <p>
 * This is typically thrown by {@link CommandRunner} when a command
 * cannot be started, times out, or is interrupted.
 */
public class ExecuteCommandException extends RuntimeException {

    /**
     * Creates a new exception with the given detail message.
     *
     * @param message description of the failure
     */
    public ExecuteCommandException(String message) {
        super(message);
    }

    /**
     * Creates a new exception with the given detail message and cause.
     *
     * @param message   description of the failure
     * @param exception underlying cause of the failure
     */
    public ExecuteCommandException(String message, Exception exception) {
        super(message, exception);
    }
}
