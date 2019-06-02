package com.example.chat;

import android.graphics.Color;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

class ColorTypeAdapter implements JsonDeserializer<Integer>
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