package com.tutao.common.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * Created by xianrui on 15/5/7.
 */

//single instance;
public class GsonHelper {
    private Gson gson;  //single instance;

    private static class GsonHelperHolder {
        public final static GsonHelper sington = new GsonHelper();
    }

    public static GsonHelper getInstance() {
        return GsonHelperHolder.sington;
    }

    private GsonHelper() {
        if (gson == null) {
            gson = new GsonBuilder()
                    .serializeSpecialFloatingPointValues()
                    .registerTypeAdapter(Double.class, new JsonSerializer<Double>() {
                        @Override
                        public JsonElement serialize(Double src, Type typeOfSrc, JsonSerializationContext context) {
                            if (src == src.longValue())
                                return new JsonPrimitive(src.longValue());
                            return new JsonPrimitive(src);
                        }
                    })
                    .setDateFormat("yyyy-MM-dd HH:mm:ss")
                    .create();
        }
    }

    public Gson getGson() {
        return gson;
    }


}
