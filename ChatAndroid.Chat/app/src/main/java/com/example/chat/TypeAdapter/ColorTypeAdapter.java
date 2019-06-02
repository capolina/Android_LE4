package com.example.chat.TypeAdapter;

import android.graphics.Color;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class ColorTypeAdapter implements JsonDeserializer<Integer>
{
    public Integer deserialize(JsonElement json, Type typeOfT,
                               JsonDeserializationContext context) throws JsonParseException
    {
        try{
            String code = json.getAsString();
            return Color.parseColor(code);
        } catch (NumberFormatException e) {
            return Color.BLACK;
        }
    }
}