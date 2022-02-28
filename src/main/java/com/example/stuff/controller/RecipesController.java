package com.example.stuff.controller;
import com.example.stuff.model.RecipeJSON;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;


@RestController
public class RecipesController {
    private static File file = new File("/Users/joannafoss/IdeaProjects/recipes-practice/data.json");
    private static JSONObject recipeJsonObj = new RecipeJSON(file).getJsonObject();

    @GetMapping("/")
    public ResponseEntity<Object> test(){
        return new ResponseEntity<>(recipeJsonObj, HttpStatus.OK);
    }

    @GetMapping("/recipes")
    public ResponseEntity<Object> recipeNames() {
        JSONArray jsonArray = (JSONArray) recipeJsonObj.get("recipes");

        JSONObject namesObj = new JSONObject();
        String[] namesArray = new String[jsonArray.size()];

        for(int i = 0; i < jsonArray.size(); i++) {
            JSONObject recipe = (JSONObject) jsonArray.get(i);
            namesArray[i] = (String) recipe.get("name");
        }

        namesObj.put("recipeNames", namesArray);
        return new ResponseEntity<>(namesObj, HttpStatus.OK);
    }

    @GetMapping("/recipe-ingredients/{name}")
    public ResponseEntity<Object> ingredients(@PathVariable("name") String name) {
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

        if(recipeDetails.isEmpty()) {
            return new ResponseEntity<>("Recipe does not exist!", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(recipeDetails, HttpStatus.OK);
    }

    @PostMapping("/recipes")
    public ResponseEntity<Object> addRecipe(@RequestBody JSONObject body) {
        try {
            JSONArray recipesArray = (JSONArray) recipeJsonObj.get("recipes");
            JSONObject newRecipesObj = new JSONObject();
            for(int i = 0; i < recipesArray.size(); i++) {
                JSONObject currentRecipe = new JSONObject((JSONObject) recipesArray.get(i));
                if(currentRecipe.get("name").equals(body.get("name"))) {
                    JSONObject response = new JSONObject();
                    response.put("error", "Recipe already exists");
                    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
                }
            }
            recipesArray.add(body);
            newRecipesObj.put("recipes", recipesArray);
            FileWriter filewriter = new FileWriter(file);
            filewriter.write(newRecipesObj.toJSONString());
            filewriter.flush();
            filewriter.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, null, HttpStatus.CREATED);
    }

    @PutMapping("/recipes")
    public ResponseEntity<Object> updateRecipe(@RequestBody JSONObject body) {
        try {
            JSONArray recipesArray = (JSONArray) recipeJsonObj.get("recipes");
            JSONObject newRecipesObj = new JSONObject();
            for(int i = 0; i < recipesArray.size(); i++) {
                JSONObject currentRecipe = new JSONObject((JSONObject) recipesArray.get(i));
                if(currentRecipe.get("name").equals(body.get("name"))) {
                    recipesArray.set(i, body);
                    newRecipesObj.put("recipes", recipesArray);
                    FileWriter filewriter = new FileWriter(file);
                    filewriter.write(newRecipesObj.toJSONString());
                    filewriter.flush();
                    filewriter.close();
                    return new ResponseEntity<>(null, null, HttpStatus.NO_CONTENT);
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        JSONObject response = new JSONObject();
        response.put("error", "Recipe does not exist");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}