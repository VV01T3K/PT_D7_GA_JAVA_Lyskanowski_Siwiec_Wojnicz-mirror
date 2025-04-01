package pl.edu.pg.networking;

import java.io.Serializable;

public class Message implements Serializable {
    public enum Prefix {
        TEXT,
        HUMAN,
        COMMAND,
    }

    public enum Command {
        EXIT,
        PING,
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