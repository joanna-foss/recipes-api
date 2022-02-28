package com.example.stuff.controller;

import com.example.stuff.model.RecipeJSON;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;


@RestController
public class RecipesController {

    private static JSONObject recipeJsonObj = new RecipeJSON(new File("/Users/joannafoss/IdeaProjects/recipes-practice/data.json")).getJsonObject();

    @GetMapping("/")
    public JSONObject test(){
        return recipeJsonObj;
    }

    @GetMapping("/recipes")
    public Object recipeNames() {
        JSONArray jsonArray = (JSONArray) recipeJsonObj.get("recipes");

        JSONObject namesObj = new JSONObject();
        String[] namesArray = new String[jsonArray.size()];

        for(int i = 0; i < jsonArray.size(); i++) {
            JSONObject recipe = (JSONObject) jsonArray.get(i);
            namesArray[i] = (String) recipe.get("name");
        }

        namesObj.put("recipeNames", namesArray);
        return namesObj;
    }
    @GetMapping("/recipe-ingredients/{name}")
    public JSONObject ingredients(@PathVariable("name") String name) {
        JSONArray jsonArray = (JSONArray) recipeJsonObj.get("recipes");

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