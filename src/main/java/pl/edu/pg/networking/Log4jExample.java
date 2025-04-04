package pl.edu.pg.networking;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class Log4jExample {

    private static Logger logger = LogManager.getLogger(Log4jExample.class);

    public static void main(String[] args) {
        logger.debug("Debug log message");
        logger.info("Info log message");
        logger.error("Error log message");
        logger.warn("Warning log message");
        logger.fatal("Fatal log message");
        logger.trace("Trace log message");
        logger.info("This is an info message");
        logger.error("This is an error message");
    }
}