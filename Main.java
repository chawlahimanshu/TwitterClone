package com.example.twitterclone;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Initialize database

        Database.getInstance().initialize();

        // Show login frame
        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}
