package com.example.twitterclone;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NewTweetPanel extends JPanel {
    private JTextArea tweetArea;
    private User user;
    private MainFrame parent;

    public NewTweetPanel(User user, MainFrame parent) {
        this.user = user;
        this.parent = parent;

        setLayout(new BorderLayout());

        tweetArea = new JTextArea();
        tweetArea.setLineWrap(true);
        tweetArea.setWrapStyleWord(true);

        add(new JLabel("Write your tweet (under 100 words):"), BorderLayout.NORTH);
        add(new JScrollPane(tweetArea), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton tweetBtn = new JButton("Tweet");
        JButton cancelBtn = new JButton("Cancel");

        tweetBtn.addActionListener((ActionEvent e) -> {
            String content = tweetArea.getText().trim();
            if (content.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tweet cannot be empty.");
                return;
            }
            if (countWords(content) > 100) {
                JOptionPane.showMessageDialog(this, "Tweet is too long. Max 100 words.");
                return;
            }
            // Add to DB
            String dateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            boolean success = Database.getInstance().addTweet(user.getId(), content, dateStr);
            if (success) {
                JOptionPane.showMessageDialog(this, "Tweet posted!");
                // Return to the all tweets panel
                parent.showAllTweetsPanel();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to post tweet.");
            }
        });

        cancelBtn.addActionListener((ActionEvent e) -> {
            // Return to the all tweets panel on cancel
            parent.showAllTweetsPanel();
        });

        buttonPanel.add(tweetBtn);
        buttonPanel.add(cancelBtn);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private int countWords(String str) {
        if (str.isEmpty()) return 0;
        String[] words = str.split("\\s+");
        return words.length;
    }
}
