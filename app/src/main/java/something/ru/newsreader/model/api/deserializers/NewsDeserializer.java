package something.ru.newsreader.model.api.deserializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import something.ru.newsreader.model.entity.News;

public class NewsDeserializer implements JsonDeserializer<News> {
    private static final String ERROR_JSON_CONTAINS_MEMBER = "News json don`t contains needed fields";

    @Override
    public News deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        News news = new News();
        JsonObject jsonObject = json.getAsJsonObject();
        if (jsonObject.has("id")) {
            news.setId(jsonObject.get("id").getAsString());
        } else {
            throw new JsonParseException(ERROR_JSON_CONTAINS_MEMBER);
        }

        if (jsonObject.has("name")) {
            news.setName(jsonObject.get("name").getAsString());
        } else {
            throw new JsonParseException(ERROR_JSON_CONTAINS_MEMBER);
        }

        if (jsonObject.has("text")) {
            news.setText(jsonObject.get("text").getAsString());
        } else {
            throw new JsonParseException(ERROR_JSON_CONTAINS_MEMBER);
        }

        if (jsonObject.has("publicationDate")) {
            JsonObject pubDateObject = jsonObject.getAsJsonObject("publicationDate");
            if (pubDateObject.has("milliseconds")) {
                news.setPublicationDate(pubDateObject.get("milliseconds").getAsLong());
            } else {
                throw new JsonParseException(ERROR_JSON_CONTAINS_MEMBER);
            }
        } else {
            throw new JsonParseException(ERROR_JSON_CONTAINS_MEMBER);
        }

        if (jsonObject.has("bankInfoTypeId")) {
            news.setBankInfoTypeId(jsonObject.get("bankInfoTypeId").getAsInt());
        } else {
            throw new JsonParseException(ERROR_JSON_CONTAINS_MEMBER);
        }
        return news;
    }
}
