package com.theironyard.charlotte;

import spark.ModelAndView;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    public static User user;
    public static Message getMessage;
    static ArrayList<Message> messages = new ArrayList<>();

    public static void main(String[] args) {

        Spark.get(
                "/",
                ((request, response) -> {
                    HashMap m = new HashMap();
                    if (user == null) {
                        return new ModelAndView(m, "index.html");
                    } else {
                        m.put("name", user.name);
                        return new ModelAndView(m, "messages.html");
                    }
                }),
                new MustacheTemplateEngine()
        );
        Spark.get(
                "/messages",
                (request, response) -> {
                    HashMap m = new HashMap();
//                    for (int i = 0 ; i < messages.size(); i++) {
//                        Message getMessages = messages.get(i);
//                    }
                     m.put("messages", messages);
                        return new ModelAndView(m, "/messages.html");
                },
                new MustacheTemplateEngine()
        );
        Spark.get(
                "/index",
                ((request, response) -> {
                    HashMap m = new HashMap();
                    if (user == null) {
                        return new ModelAndView(m, "index.html");
                    } else {
                        m.put("name", user.name);
                        return new ModelAndView(m, "/");
                    }


                }),
                new MustacheTemplateEngine()
        );

        Spark.post(
                "/create-user",
                ((request, response) -> {
                    String name = request.queryParams("loginName");
                    user = new User(name);
                    response.redirect("/messages");
                    return "";
                })
        );
        Spark.post(
                "/create-message",
                ((request, response) -> {
                    String theMessage = request.queryParams("message");
                    getMessage = new Message(theMessage);
                    messages.add(getMessage);
                    response.redirect("/messages");
                    return "";
                }));

    }
}
