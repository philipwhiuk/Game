package com.whiuk.philip.mmorpg.client;

import org.apache.log4j.PropertyConfigurator;

/**
 * @author Philip
 */
public final class Main {
    /**
     * Utility class.
     */
    private Main() {
    }

    /**
     * @param args
     *            Command line arguments
     */
    public static void main(final String[] args) {
        fixJavaLogging();
        // Setup Log4J logging
        PropertyConfigurator.configure("log4j.properties");
        GameClient client = new GameClient();
        GameClient.setGameClient(client);
        client.run();
    }

    /**
     * Fix Java Logging API usage.
     */
    private static void fixJavaLogging() {
        // Fix Java Logging
        java.util.logging.Logger topLogger = java.util.logging.Logger
                .getLogger("");
        // Handler for console (reuse it if it already exists)
        java.util.logging.Handler consoleHandler = null;
        // see if there is already a console handler
        for (java.util.logging.Handler handler : topLogger.getHandlers()) {
            if (handler instanceof java.util.logging.ConsoleHandler) {
                // found the console handler
                consoleHandler = handler;
                break;
            }
        }
        if (consoleHandler == null) {
            // there was no console handler found, create a new one
            consoleHandler = new java.util.logging.ConsoleHandler();
            topLogger.addHandler(consoleHandler);
        }
        // set the console handler to fine:
        consoleHandler.setLevel(java.util.logging.Level.WARNING);

    }

}
