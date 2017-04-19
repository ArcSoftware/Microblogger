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
    public static ArrayList<Message> matches = new ArrayList<>();

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
            return gson.toJson(messages); // gson for the win!
//            return new JsonSerializer().serialize(messages); <- generates empty stuff.
        });

        Spark.post("/api/messages", (req, res) -> {
            Message message = gson.fromJson(req.body(), Message.class);
//            Message message = new JsonParser().parse(req.body(), Message.class); <- to complex
            messages.add(message);

            return "";
        });
        Spark.post("/api/messages/:name", (req, res) -> {
            String searchName = req.queryParams("searchName");
            matches.clear();
            System.out.println("Sorting messages by name." + searchName);

            for (int i = 0; i < messages.size(); i++) {
                Message search = messages.get(i);
                if (search.userName.equals(searchName)) {
                    matches.add(search);
                }
            }
            System.out.println(matches.size());
            res.redirect("/");
            return "";
//
        });

    }
}
