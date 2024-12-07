package com.example.twitterclone;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private static Database instance;
    private Connection conn;

    private Database() { }

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    public void initialize() {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:twitter_clone.db");
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("CREATE TABLE IF NOT EXISTS users ("
                        + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + "username TEXT UNIQUE NOT NULL,"
                        + "password TEXT NOT NULL)");

                stmt.execute("CREATE TABLE IF NOT EXISTS tweets ("
                        + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + "user_id INTEGER NOT NULL,"
                        + "content TEXT NOT NULL,"
                        + "date_created TEXT NOT NULL,"
                        + "FOREIGN KEY(user_id) REFERENCES users(id))");

                stmt.execute("CREATE TABLE IF NOT EXISTS comments ("
                        + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + "tweet_id INTEGER NOT NULL,"
                        + "user_id INTEGER NOT NULL,"
                        + "content TEXT NOT NULL,"
                        + "date_created TEXT NOT NULL,"
                        + "FOREIGN KEY(tweet_id) REFERENCES tweets(id),"
                        + "FOREIGN KEY(user_id) REFERENCES users(id))");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return conn;
    }

    public User login(String username, String password) {
        String sql = "SELECT id, username, password FROM users WHERE username=? AND password=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(rs.getInt("id"), rs.getString("username"), rs.getString("password"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean register(String username, String password) {
        String sql = "INSERT INTO users(username,password) VALUES(?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addTweet(int userId, String content, String dateCreated) {
        String sql = "INSERT INTO tweets(user_id, content, date_created) VALUES(?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, content);
            ps.setString(3, dateCreated);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addComment(int tweetId, int userId, String content, String dateCreated) {
        String sql = "INSERT INTO comments(tweet_id, user_id, content, date_created) VALUES(?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, tweetId);
            ps.setInt(2, userId);
            ps.setString(3, content);
            ps.setString(4, dateCreated);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Comment> getCommentsForTweet(int tweetId) {
        List<Comment> comments = new ArrayList<>();
        String sql = "SELECT c.id, c.tweet_id, c.user_id, c.content, c.date_created, u.username " +
                "FROM comments c " +
                "JOIN users u ON c.user_id = u.id " +
                "WHERE c.tweet_id=? ORDER BY c.id ASC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, tweetId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Comment comment = new Comment(
                            rs.getInt("id"),
                            rs.getInt("tweet_id"),
                            rs.getInt("user_id"),
                            rs.getString("content"),
                            rs.getString("date_created"),
                            rs.getString("username")
                    );
                    comments.add(comment);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return comments;
    }

    public boolean deleteTweet(int tweetId) {
        // If foreign keys are enforced, and you want to remove all associated comments first:
        // You can do something like:
        // try (PreparedStatement ps = conn.prepareStatement("DELETE FROM comments WHERE tweet_id=?")) {
        //     ps.setInt(1, tweetId);
        //     ps.executeUpdate();
        // }

        String sql = "DELETE FROM tweets WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, tweetId);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateComment(int commentId, String newContent) {
        String sql = "UPDATE comments SET content=? WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newContent);
            ps.setInt(2, commentId);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteComment(int commentId) {
        String sql = "DELETE FROM comments WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, commentId);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Tweet> getAllTweets() {
        List<Tweet> tweets = new ArrayList<>();
        String sql = "SELECT t.id, t.user_id, t.content, t.date_created, u.username "
                + "FROM tweets t "
                + "JOIN users u ON t.user_id = u.id "
                + "ORDER BY t.id DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Tweet t = new Tweet(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("content"),
                        rs.getString("date_created"),
                        rs.getString("username")
                );
                tweets.add(t);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tweets;
    }

    public List<Tweet> getUserTweets(int userId) {
        List<Tweet> tweets = new ArrayList<>();
        String sql = "SELECT t.id, t.user_id, t.content, t.date_created, u.username "
                + "FROM tweets t "
                + "JOIN users u ON t.user_id = u.id "
                + "WHERE t.user_id=? ORDER BY t.id DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Tweet t = new Tweet(
                            rs.getInt("id"),
                            rs.getInt("user_id"),
                            rs.getString("content"),
                            rs.getString("date_created"),
                            rs.getString("username")
                    );
                    tweets.add(t);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tweets;
    }
}
