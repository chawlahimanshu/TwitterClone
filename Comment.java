package com.example.twitterclone;

public class Comment {
    private int id;
    private int tweetId;
    private int userId;
    private String content;
    private String dateCreated;
    private String username;

    public Comment(int id, int tweetId, int userId, String content, String dateCreated, String username) {
        this.id = id;
        this.tweetId = tweetId;
        this.userId = userId;
        this.content = content;
        this.dateCreated = dateCreated;
        this.username = username;
    }

    public int getId() { return id; }
    public int getTweetId() { return tweetId; }
    public int getUserId() { return userId; }
    public String getContent() { return content; }
    public String getDateCreated() { return dateCreated; }
    public String getUsername() { return username; }
}
