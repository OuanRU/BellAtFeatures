package logger;

import org.slf4j.LoggerFactory;

public class Logger {
    private static org.slf4j.Logger logger = LoggerFactory.getLogger("TESTS");

    public static void info(String message) {
        logger.info("\u001B[32m" + message);
    }

    public static void info(String message, Object ... params) {
        logger.info("\u001B[32m" + message, params);
    }

    public static void trace(String message) {
        logger.trace("\u001B[36m" + message);
    }

    public static void trace(String message, Object ... params) {
        logger.trace("\u001B[36m" + message, params);
    }

    public static void debug(String message) {
        logger.debug("\u001B[35m" + message);
    }

    public static void debug(String message, Object ... params) {
        logger.debug("\u001B[35m" + message, params);
    }

    public static void warn(String message) {
        logger.warn("\u001B[33m" + message);
    }

    public static void warn(String message, Object ... params) {
        logger.warn("\u001B[33m" + message, params);
    }

    public static void error(String message) {
        logger.error("\u001B[31m" + message);
    }

    public static void error(String message, Object ... params) {
        logger.error("\u001B[31m" + message, params);
    }
}
