package pl.edu.pg.networking;

import java.io.Serializable;

public class Message implements Serializable {
    public enum Prefix {
        NAME,
        TEXT,
        HUMAN,
        COMMAND,
        RESPONSE,
    }

    public enum Command {
        EXIT,
        PING,
        LAST_HUMAN,
        CLEAN_LOGS,
    }

    public enum Response {
        OK,
        PING,
        EXITED,
        INVALID_PREFIX,
        INVALID_CONTENT,
        INVALID_COMMAND,
        CONNECTION_ERROR,
        ERROR,
    }

    public Prefix prefix;
    public Object content;

    public Message(Prefix prefix, Object content) {
        this.prefix = prefix;
        this.content = content;
    }
}