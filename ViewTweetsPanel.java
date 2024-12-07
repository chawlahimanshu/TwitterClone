package com.example.twitterclone;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ViewTweetsPanel extends JPanel {

    public ViewTweetsPanel(User user) {
        setLayout(new BorderLayout());

        // Retrieve the tweets for the given user ID
        List<Tweet> tweets = Database.getInstance().getUserTweets(user.getId());

        // Create a list model to display the tweets in a JList
        DefaultListModel<String> listModel = new DefaultListModel<>();

        // Populate the list model with each tweet's date, username, and content
        for (Tweet t : tweets) {
            String entry = t.getDateCreated() + " | @" + t.getUsername() + ": " + t.getContent();
            listModel.addElement(entry);
        }

        // Create a JList to hold the tweets and wrap it in a scroll pane
        JList<String> tweetList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(tweetList);

        // Add a label at the top
        add(new JLabel("Your Tweets:"), BorderLayout.NORTH);

        // Add the scrollable list in the center
        add(scrollPane, BorderLayout.CENTER);
    }
}
