package com.theironyard.charlotte;

import spark.ModelAndView;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    public static User user;

    public static void main(String[] args) {


        Spark.get(
                "/",
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
        Spark.get(
                "/messages",
                ((request, response) -> {
                    HashMap m = new HashMap();
                    if (user == null) {
                        return new ModelAndView(m, "messages.html");
                    } else {
                        m.put("name", user.name);
                        return new ModelAndView(m, "/");
                    }


                }),
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
                    response.redirect("/");
                    return "";
                })
        );
        Spark.post(
                "/create-message",
                ((request, response) -> {
                    ArrayList<String> namesLocal = new ArrayList<>();
                    response.redirect("/");
                    return "";
                }));

    }
}
