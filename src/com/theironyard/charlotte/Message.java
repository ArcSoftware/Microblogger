package com.theironyard.charlotte;

/**
 * Created by Jake on 4/13/17.
 */
public class Message {
    String text;
    User user;

    public Message(String text, User user) {
        this.text = text;
        this.user = user;
    }
}
