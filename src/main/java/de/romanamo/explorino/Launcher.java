package de.romanamo.explorino;

import java.util.logging.Logger;

public final class Launcher {

    /**
     * Logger name.
     */
    private static final String LOGGER_NAME = "Explorino";

    /**
     * Gets the {@link Logger}.
     *
     * @return logger
     */
    public static Logger getLogger() {
        return Logger.getLogger(LOGGER_NAME);
    }
    private Launcher() {
    }

    /**
     * Main method.
     *
     * @param args arguments
     */
    public static void main(String[] args) {
        Main.main(args);
    }
}
