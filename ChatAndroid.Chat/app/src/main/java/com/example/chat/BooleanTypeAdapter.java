package com.example.chat;

import java.lang.reflect.Type;
import com.google.gson.*;
class BooleanTypeAdapter implements JsonDeserializer<Boolean>
{
    public Boolean deserialize(JsonElement json, Type typeOfT,
                               JsonDeserializationContext context) throws JsonParseException
    {
        try{
            int code = json.getAsInt();
            return code == 0 ? false :
                code == 1 ? true :
                        null;
        } catch (NumberFormatException e) {
            String code = json.getAsString();
            return code == "false" ? false :
                    code == "true" ? true :
                            null;
        }
    }
}