package com.mms.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.*;
import java.awt.*;

public class DebugUtils {
    private static JFrame debugFrame;
    private static JTextArea debugArea;
    private static boolean initialized = false;
    
    public static void initialize() {
        if (initialized) return;
        
        SwingUtilities.invokeLater(() -> {
            createDebugWindow();
            initialized = true;
        });
    }
    
    private static void createDebugWindow() {
        debugFrame = new JFrame("🐛 MMS Debug Console");
        debugFrame.setSize(600, 400);
        debugFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        
        // Position debug window on the right side of screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        debugFrame.setLocation(screenSize.width - 620, 50);
        
        debugArea = new JTextArea();
        debugArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        debugArea.setBackground(Color.BLACK);
        debugArea.setForeground(Color.GREEN);
        debugArea.setEditable(false);
        
        JScrollPane scrollPane = new JScrollPane(debugArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton clearBtn = new JButton("Clear");
        JButton hideBtn = new JButton("Hide");
        JButton showMainBtn = new JButton("Show Main App");
        
        clearBtn.addActionListener(e -> debugArea.setText(""));
        hideBtn.addActionListener(e -> debugFrame.setVisible(false));
        showMainBtn.addActionListener(e -> {
            // Bring main application windows to front
            for (Frame frame : Frame.getFrames()) {
                if (frame != debugFrame && frame.isVisible()) {
                    frame.toFront();
                }
            }
        });
        
        buttonPanel.add(clearBtn);
        buttonPanel.add(hideBtn);
        buttonPanel.add(showMainBtn);
        
        debugFrame.add(scrollPane, BorderLayout.CENTER);
        debugFrame.add(buttonPanel, BorderLayout.SOUTH);
        
        debugFrame.setVisible(true);
        
        log("🐛 Debug console initialized");
        log("💡 This window shows real-time application logs");
    }
    
    public static void log(String message) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        String logMessage = String.format("[%s] %s%n", timestamp, message);
        
        // Console output
        System.out.print(logMessage);
        
        // Debug window output (if initialized)
        if (initialized && debugArea != null) {
            SwingUtilities.invokeLater(() -> {
                debugArea.append(logMessage);
                debugArea.setCaretPosition(debugArea.getDocument().getLength());
            });
        }
    }
    
    public static void logError(String message, Exception e) {
        log("❌ ERROR: " + message);
        if (e != null) {
            log("   Exception: " + e.getMessage());
        }
    }
    
    public static void logSuccess(String message) {
        log("✅ " + message);
    }
    
    public static void logInfo(String message) {
        log("ℹ️ " + message);
    }
    
    public static void logWarning(String message) {
        log("⚠️ " + message);
    }
    
    public static void showDebugWindow() {
        if (debugFrame != null) {
            debugFrame.setVisible(true);
            debugFrame.toFront();
        }
    }
    
    public static void hideDebugWindow() {
        if (debugFrame != null) {
            debugFrame.setVisible(false);
        }
    }
    
    // Utility method to log database operations
    public static void logDatabaseOperation(String operation, boolean success) {
        if (success) {
            logSuccess("Database: " + operation);
        } else {
            logError("Database: " + operation + " failed", null);
        }
    }
    
    // Utility method to log UI actions
    public static void logUIAction(String action) {
        log("🖱️ UI: " + action);
    }
}
