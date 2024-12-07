package com.example.twitterclone;

public class Tweet {
    private int id;
    private int userId;
    private String content;
    private String dateCreated;
    private String username; // To display which user posted the tweet

    public Tweet(int id, int userId, String content, String dateCreated, String username) {
        this.id = id;
        this.userId = userId;
        this.content = content;
        this.dateCreated = dateCreated;
        this.username = username;
    }

    public int getId() { return id; }
    public int getUserId() { return userId; }
    public String getContent() { return content; }
    public String getDateCreated() { return dateCreated; }
    public String getUsername() { return username; }
}
