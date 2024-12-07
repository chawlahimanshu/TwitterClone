package com.example.twitterclone;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private User currentUser;
    private JPanel logoutPanel;
    private JPanel navPanel;

    public MainFrame(User user) {
        this.currentUser = user;
        setTitle("Welcome, " + user.getUsername());
        setSize(500,400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Bottom "back" button that returns to all tweets view
        logoutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton backBtn = new JButton("← Back");
        backBtn.addActionListener((e) -> {
            showAllTweetsPanel();
        });
        logoutPanel.add(backBtn);

        // Top navigation panel with additional buttons
        navPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton allTweetsBtn = new JButton("All Tweets");
        allTweetsBtn.addActionListener((e) -> showAllTweetsPanel());
        navPanel.add(allTweetsBtn);

        JButton myTweetsBtn = new JButton("My Tweets");
        myTweetsBtn.addActionListener((e) -> showMyTweetsPanel());
        navPanel.add(myTweetsBtn);

        JButton newTweetBtn = new JButton("New Tweet");
        newTweetBtn.addActionListener((e) -> showNewTweetPanel());
        navPanel.add(newTweetBtn);

        // Add the logout button at the top right next to other options
        JButton logoutBtn = new JButton("Log Out");
        logoutBtn.addActionListener((e) -> {
            // Close the current frame
            dispose();
            // Reopen the login frame
            new LoginFrame().setVisible(true);
        });
        navPanel.add(logoutBtn);

        // Display all tweets by default when logged in
        showAllTweetsPanel();
    }

    public void showAllTweetsPanel() {
        replaceContent(new AllTweetsPanel(currentUser));
    }


    public void showMyTweetsPanel() {
        replaceContent(new ViewTweetsPanel(currentUser));
    }

    public void showNewTweetPanel() {
        NewTweetPanel panel = new NewTweetPanel(currentUser, this);
        replaceContent(panel);
    }



    private void replaceContent(JComponent comp) {
        getContentPane().removeAll();
        add(navPanel, BorderLayout.NORTH);
        add(comp, BorderLayout.CENTER);
        add(logoutPanel, BorderLayout.SOUTH);
        revalidate();
        repaint();
    }
}
