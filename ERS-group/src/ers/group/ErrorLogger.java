package ers.group;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Centralized error logging utility for the ERS system.
 * Logs errors to both console and file with timestamps and context.
 */
public class ErrorLogger {
    private static final String LOG_FILE_PATH = "master files/ers_error.log";
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final boolean CONSOLE_OUTPUT_ENABLED = true;
    
    /**
     * Log an error with context and exception details
     * @param context Description of where/what was happening when error occurred
     * @param e The exception that was caught
     */
    public static void logError(String context, Exception e) {
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
        String message = String.format("[ERROR] %s | %s: %s", timestamp, context, e.getMessage());
        
        // Console output for development
        if (CONSOLE_OUTPUT_ENABLED) {
            System.err.println(message);
            e.printStackTrace(System.err);
        }
        
        // File output for production logging
        writeToLogFile(message, e);
    }
    
    /**
     * Log a warning message without exception
     * @param message The warning message
     */
    public static void logWarning(String message) {
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
        String logMessage = String.format("[WARNING] %s | %s", timestamp, message);
        
        if (CONSOLE_OUTPUT_ENABLED) {
            System.err.println(logMessage);
        }
        
        writeToLogFile(logMessage, null);
    }
    
    /**
     * Log an informational message
     * @param message The info message
     */
    public static void logInfo(String message) {
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
        String logMessage = String.format("[INFO] %s | %s", timestamp, message);
        
        if (CONSOLE_OUTPUT_ENABLED) {
            System.out.println(logMessage);
        }
        
        writeToLogFile(logMessage, null);
    }
    
    /**
     * Write log entry to file
     */
    private static void writeToLogFile(String message, Exception e) {
        try {
            String[] possiblePaths = {
                "ERS-group/src/ers/group/master files/ers_error.log",
                "src/ers/group/master files/ers_error.log",
                "master files/ers_error.log",
                "ers_error.log"
            };
            
            String logPath = FilePathResolver.resolveWritablePath(possiblePaths);
            
            // Append to log file
            try (PrintWriter writer = new PrintWriter(new FileWriter(logPath, true))) {
                writer.println(message);
                
                if (e != null) {
                    // Write stack trace
                    StringWriter sw = new StringWriter();
                    e.printStackTrace(new PrintWriter(sw));
                    writer.println(sw.toString());
                }
                
                writer.println(); // Blank line between entries
            }
        } catch (IOException ioException) {
            // Fallback to console only if file writing fails
            System.err.println("Failed to write to log file: " + ioException.getMessage());
        }
    }
    
    /**
     * Clear the log file (useful for testing)
     */
    public static void clearLog() {
        try {
            String[] possiblePaths = {
                "ERS-group/src/ers/group/master files/ers_error.log",
                "src/ers/group/master files/ers_error.log",
                "master files/ers_error.log",
                "ers_error.log"
            };
            
            String logPath = FilePathResolver.resolveWritablePath(possiblePaths);
            
            try (PrintWriter writer = new PrintWriter(new FileWriter(logPath, false))) {
                writer.println("=== ERS Error Log ===");
                writer.println("Log cleared at: " + LocalDateTime.now().format(TIMESTAMP_FORMAT));
                writer.println();
            }
        } catch (IOException e) {
            System.err.println("Failed to clear log file: " + e.getMessage());
        }
    }
}
