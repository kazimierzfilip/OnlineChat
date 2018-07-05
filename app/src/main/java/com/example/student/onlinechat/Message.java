package com.example.student.onlinechat;

/**
 * Created by Kazik on 2018-07-05.
 */

public class Message {
    private String message;
    private String author;

    public Message(String message, String author) {
        this.message = message;
        this.author = author;
    }

    public Message() {
    }

    public String getMessage() {
        return message;
    }

    public String getAuthor() {
        return author;
    }
}
