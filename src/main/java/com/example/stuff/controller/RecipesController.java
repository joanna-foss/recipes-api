package com.example.stuff.controller;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;


@RestController
public class RecipesController {
    public JSONObject RecipesController() {
        JSONObject recipesJSONObj = new JSONObject();
            try {
                JSONParser jsonparser = new JSONParser();
                BufferedReader bufferedReader = new BufferedReader(new FileReader("/Users/joannafoss/IdeaProjects/recipes-practice/data.json"));
                Object obj = jsonparser.parse(bufferedReader);
                recipesJSONObj = (JSONObject) obj;
            } catch (Exception e) {
                e.printStackTrace();
            }
        return recipesJSONObj;
    }

    @GetMapping("/")
    public JSONObject test(){
        JSONObject test = RecipesController();
        return test;
    }
    @GetMapping("/recipes")
    public Object allInfo() throws IOException, ParseException {
        JSONParser jsonparser = new JSONParser();
        BufferedReader bufferedReader = new BufferedReader(new FileReader("/Users/joannafoss/IdeaProjects/recipes-practice/data.json"));
        Object obj = jsonparser.parse(bufferedReader);
        JSONObject jsonObject = (JSONObject) obj;

        return jsonObject.get("recipes");
    }

    @GetMapping("/recipe-names")
    public JSONObject names() {
        JSONObject recipesJSONObj = RecipesController();
        JSONArray jsonArray = (JSONArray) recipesJSONObj.get("recipes");

        JSONObject namesObject = new JSONObject();
        String[] stringArray = new String[jsonArray.size()];
        for(int i = 0; i < jsonArray.size(); i++) {
            JSONObject recipe = (JSONObject) jsonArray.get(i);
            stringArray[i] = (String) recipe.get("name");
        }
        namesObject.put("recipeNames", stringArray);
        return namesObject;
    }

    @GetMapping("/recipe-ingredients/{name}")
    public JSONObject ingredients(@PathVariable("name") String name) {
        JSONObject recipesJSONObj = RecipesController();
        JSONArray jsonArray = (JSONArray) recipesJSONObj.get("recipes");

        JSONObject recipeDetails = new JSONObject();
        JSONObject detailsObj = new JSONObject();

        for(int i = 0; i < jsonArray.size(); i++) {
            JSONObject recipe = (JSONObject) jsonArray.get(i);
            if(recipe.get("name").equals(name)) {
                JSONArray currentIngredients = (JSONArray) recipe.get("ingredients");
                int steps = recipe.size();
                detailsObj.put("ingredients", currentIngredients);
                detailsObj.put("numSteps", steps);
                recipeDetails.put("details", detailsObj);
            }
        }

        return recipeDetails;
    }
}