package com.example.twitterclone;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginFrame() {
        setTitle("Login");
        setSize(400,300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(10,10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        // Title label at the top
        JLabel titleLabel = new JLabel("Please Log In", SwingConstants.CENTER);
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
        centerPanel.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        usernameField = new JTextField(15);
        centerPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        centerPanel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        centerPanel.add(passwordField, gbc);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Bottom panel with login and register buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT,10,10));
        JButton loginBtn = new JButton("Login");
        JButton registerBtn = new JButton("Register");

        loginBtn.addActionListener((ActionEvent e) -> {
            String user = usernameField.getText().trim();
            String pass = new String(passwordField.getPassword()).trim();
            User loggedIn = Database.getInstance().login(user, pass);
            if (loggedIn != null) {
                // success
                dispose();
                new MainFrame(loggedIn).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials");
            }
        });

        registerBtn.addActionListener((ActionEvent e) -> {
            // Open Register Frame
            dispose();
            new RegisterFrame().setVisible(true);
        });

        buttonPanel.add(loginBtn);
        buttonPanel.add(registerBtn);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }
}
