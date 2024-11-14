package org.landon.editor.windows.logger;

public class Log {

    private final String message;
    private final LogType type;

    public Log(String message, LogType type) {
        this.message = message;
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public LogType getType() {
        return type;
    }

    public enum LogType {

        INFO,
        WARNING,
        ERROR

    }

}
