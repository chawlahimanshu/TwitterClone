package com.example.twitterclone;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AllTweetsPanel extends JPanel {
    private User currentUser;

    public AllTweetsPanel(User currentUser) {
        this.currentUser = currentUser;
        setLayout(new BorderLayout());
        refreshAllTweets();
    }

    private void refreshAllTweets() {
        removeAll(); // Clear old components

        List<Tweet> tweets = Database.getInstance().getAllTweets();
        JPanel tweetsContainer = new JPanel();
        tweetsContainer.setLayout(new BoxLayout(tweetsContainer, BoxLayout.Y_AXIS));

        for (Tweet t : tweets) {
            JPanel tweetPanel = new JPanel();
            tweetPanel.setLayout(new BoxLayout(tweetPanel, BoxLayout.Y_AXIS));
            tweetPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(10,10,10,10),
                    BorderFactory.createLineBorder(Color.LIGHT_GRAY)
            ));

            // Tweet info panel
            JPanel tweetInfoPanel = new JPanel(new BorderLayout());
            String entry = t.getDateCreated() + " | @" + t.getUsername() + ": " + t.getContent();
            JLabel tweetLabel = new JLabel(entry);

            JPanel rightButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));

            // Comment button
            JButton commentBtn = new JButton("Comment");
            commentBtn.addActionListener((ActionEvent e) -> {
                String comment = JOptionPane.showInputDialog(this, "Enter your comment:");
                if (comment != null && !comment.trim().isEmpty()) {
                    String dateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                    boolean success = Database.getInstance().addComment(t.getId(), currentUser.getId(), comment.trim(), dateStr);
                    if (success) {
                        JOptionPane.showMessageDialog(this, "Comment added!");
                        refreshCommentsForTweet(t, tweetPanel);
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to add comment.");
                    }
                }
            });
            rightButtonPanel.add(commentBtn);

            // If current user is the author of the tweet, show a "Delete Tweet" button
            if (t.getUserId() == currentUser.getId()) {
                JButton deleteTweetBtn = new JButton("Delete Tweet");
                deleteTweetBtn.addActionListener((ActionEvent e) -> {
                    int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this tweet?",
                            "Confirm Delete", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        boolean deleted = Database.getInstance().deleteTweet(t.getId());
                        if (deleted) {
                            JOptionPane.showMessageDialog(this, "Tweet deleted!");
                            refreshAllTweets(); // Refresh the entire feed
                        } else {
                            JOptionPane.showMessageDialog(this, "Failed to delete tweet.");
                        }
                    }
                });
                rightButtonPanel.add(deleteTweetBtn);
            }

            tweetInfoPanel.add(tweetLabel, BorderLayout.CENTER);
            tweetInfoPanel.add(rightButtonPanel, BorderLayout.EAST);

            tweetPanel.add(tweetInfoPanel);
            addCommentsForTweet(t, tweetPanel);

            tweetsContainer.add(tweetPanel);
        }

        JScrollPane scrollPane = new JScrollPane(tweetsContainer);
        add(new JLabel("All Tweets:"), BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        revalidate();
        repaint();
    }

    private void addCommentsForTweet(Tweet tweet, JPanel tweetPanel) {
        JPanel commentsContainer = new JPanel();
        commentsContainer.setLayout(new BoxLayout(commentsContainer, BoxLayout.Y_AXIS));
        commentsContainer.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 0));

        List<Comment> comments = Database.getInstance().getCommentsForTweet(tweet.getId());
        for (Comment c : comments) {
            commentsContainer.add(createCommentPanel(c, tweet));
        }

        removeOldCommentsPanel(tweetPanel);
        tweetPanel.add(commentsContainer);
        tweetPanel.revalidate();
        tweetPanel.repaint();
    }

    private JPanel createCommentPanel(Comment c, Tweet tweet) {
        JPanel commentPanel = new JPanel(new BorderLayout());
        String commentEntry = c.getDateCreated() + " | @" + c.getUsername() + ": " + c.getContent();
        JLabel commentLabel = new JLabel(commentEntry);
        commentLabel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        commentPanel.add(commentLabel, BorderLayout.CENTER);

        // Edit/Delete if this user owns the comment
        if (c.getUserId() == currentUser.getId()) {
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));

            JButton editBtn = new JButton("Edit");
            editBtn.addActionListener((ActionEvent e) -> {
                String newContent = JOptionPane.showInputDialog(commentPanel, "Edit your comment:", c.getContent());
                if (newContent != null && !newContent.trim().isEmpty()) {
                    boolean updated = Database.getInstance().updateComment(c.getId(), newContent.trim());
                    if (updated) {
                        JOptionPane.showMessageDialog(commentPanel, "Comment updated!");
                        refreshCommentsForTweet(tweet, (JPanel)commentPanel.getParent().getParent());
                    } else {
                        JOptionPane.showMessageDialog(commentPanel, "Failed to update comment.");
                    }
                }
            });
            buttonPanel.add(editBtn);

            JButton deleteBtn = new JButton("Delete");
            deleteBtn.addActionListener((ActionEvent e) -> {
                int confirm = JOptionPane.showConfirmDialog(commentPanel, "Are you sure you want to delete this comment?",
                        "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    boolean deleted = Database.getInstance().deleteComment(c.getId());
                    if (deleted) {
                        JOptionPane.showMessageDialog(commentPanel, "Comment deleted!");
                        refreshCommentsForTweet(tweet, (JPanel)commentPanel.getParent().getParent());
                    } else {
                        JOptionPane.showMessageDialog(commentPanel, "Failed to delete comment.");
                    }
                }
            });
            buttonPanel.add(deleteBtn);

            commentPanel.add(buttonPanel, BorderLayout.EAST);
        }

        return commentPanel;
    }

    private void removeOldCommentsPanel(JPanel tweetPanel) {
        while (tweetPanel.getComponentCount() > 1) {
            tweetPanel.remove(1);
        }
    }

    private void refreshCommentsForTweet(Tweet tweet, JPanel tweetPanel) {
        addCommentsForTweet(tweet, tweetPanel);
    }
}
