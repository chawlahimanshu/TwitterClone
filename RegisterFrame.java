package com.example.twitterclone;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class RegisterFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public RegisterFrame() {
        setTitle("Register");
        setSize(400,300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(10,10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        // Title label at the top
        JLabel titleLabel = new JLabel("Create an Account", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Center panel for username and password fields
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        centerPanel.add(new JLabel("New Username:"), gbc);

        gbc.gridx = 1;
        usernameField = new JTextField(15);
        centerPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        centerPanel.add(new JLabel("New Password:"), gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        centerPanel.add(passwordField, gbc);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Bottom panel with buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT,10,10));
        JButton registerBtn = new JButton("Create Account");
        JButton backBtn = new JButton("Back to Login");

        registerBtn.addActionListener((ActionEvent e) -> {
            String user = usernameField.getText().trim();
            String pass = new String(passwordField.getPassword()).trim();
            boolean success = Database.getInstance().register(user, pass);
            if (success) {
                JOptionPane.showMessageDialog(this, "Account created. Please log in.");
                dispose();
                new LoginFrame().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Username already taken or an error occurred.");
            }
        });

        backBtn.addActionListener((ActionEvent e) -> {
            dispose();
            new LoginFrame().setVisible(true);
        });

        buttonPanel.add(registerBtn);
        buttonPanel.add(backBtn);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }
}
