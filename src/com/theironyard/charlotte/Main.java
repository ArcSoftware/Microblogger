package com.theironyard.charlotte;

import com.google.gson.Gson;
import jodd.json.JsonParser;
import jodd.json.JsonSerializer;
import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    public static Gson gson = new Gson();
    public static HashMap<String, User> users = new HashMap<>();
    public static Message getMessage;
    public static ArrayList<Message> messages = new ArrayList<>();

    public static void main(String[] args) {
        Spark.staticFileLocation("/public");

        Spark.get(
                "/",
                ((request, response) -> {
                    HashMap m = new HashMap();
                    Session session = request.session();

                    String userName = session.attribute("userName");
                    User user = users.get(userName);

                    if (user == null) {
                        return new ModelAndView(m, "index.html");
                    } else {
                        m.put("name", user.name);
                        m.put("messages", messages);
                        return new ModelAndView(m, "messages.html");
                    }
                }),
                new MustacheTemplateEngine()
        );

        Spark.post(
                "/create-user",
                ((request, response) -> {
                    String name = request.queryParams("loginName");
                    User user = users.get(name);
                    if (user == null) {
                        user = new User(name);
                        users.put(name, user);
                    }
                    Session session = request.session();
                    session.attribute("userName", name);

                    response.redirect("/");
                    return "";
                })
        );
        Spark.post(
                "/create-message",
                ((request, response) -> {
                    String theMessage = request.queryParams("message");
                    Session session = request.session();
                    String userName = session.attribute("userName");
                    getMessage = new Message(theMessage, userName);
                    messages.add(getMessage);
                    response.redirect("/");
                    return "";
                }));

        Spark.get("/api/messages", (req, res) -> {
            System.out.println(messages.get(0).text);
            return gson.toJson(messages);
//            return new JsonSerializer().serialize(messages);
        });

        Spark.post("/api/messages", (req, res) -> {
            Message message = new JsonParser().parse(req.body(), Message.class);

            messages.add(message);

            return "";
        });

    }
}
