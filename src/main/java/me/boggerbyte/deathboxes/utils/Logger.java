package me.boggerbyte.deathboxes.utils;

import me.boggerbyte.deathboxes.Main;

import java.util.logging.Level;

public class Logger {
    private static final java.util.logging.Logger logger = Main.getInstance().getLogger();

    public static void log(Level level, String message) {
        logger.log(level, message);
    }
}
