package com.example.stuff.model;

import lombok.Data;
import lombok.SneakyThrows;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

@Data
public class RecipeJSON {
    private JSONObject jsonObject;

    @SneakyThrows
    public RecipeJSON(File file) {
        JSONParser jsonparser = new JSONParser();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        Object obj = jsonparser.parse(bufferedReader);
        JSONObject recipeJSON = (JSONObject) obj;
        this.jsonObject = recipeJSON;
    }
}