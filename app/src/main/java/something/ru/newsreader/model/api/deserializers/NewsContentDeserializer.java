package something.ru.newsreader.model.api.deserializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import something.ru.newsreader.model.entity.NewsContent;

public class NewsContentDeserializer implements JsonDeserializer<NewsContent> {
    private static final String ERROR_JSON_CONTAINS_MEMBER = "NewsContent json " +
            "don`t contains needed fields";

    @Override
    public NewsContent deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        NewsContent newsContent = new NewsContent();
        JsonObject jsonObject = json.getAsJsonObject();

        if (jsonObject.has("title")) {
            JsonObject titleObject = jsonObject.getAsJsonObject("title");
            if (titleObject.has("id")) {
                newsContent.setId(titleObject.get("id").getAsString());
            } else {
                throw new JsonParseException(ERROR_JSON_CONTAINS_MEMBER);
            }
        } else {
            throw new JsonParseException(ERROR_JSON_CONTAINS_MEMBER);
        }

        if (jsonObject.has("creationDate")) {
            JsonObject creationDate = jsonObject.getAsJsonObject("creationDate");
            if (creationDate.has("milliseconds")) {
                newsContent.setCreationDate(DeserializerUtils.createDateFromLong(
                        creationDate.get("milliseconds").getAsLong()));
            } else {
                throw new JsonParseException(ERROR_JSON_CONTAINS_MEMBER);
            }
        }

        if (jsonObject.has("lastModificationDate")) {
            JsonObject lastModifyDateObject = jsonObject.getAsJsonObject("lastModificationDate");
            if (lastModifyDateObject.has("milliseconds")) {
                newsContent.setLastModificationDate(DeserializerUtils.createDateFromLong(
                        lastModifyDateObject.get("milliseconds").getAsLong()));
            } else {
                throw new JsonParseException(ERROR_JSON_CONTAINS_MEMBER);
            }
        } else {
            throw new JsonParseException(ERROR_JSON_CONTAINS_MEMBER);
        }

        if (jsonObject.has("content")) {
            newsContent.setContent(jsonObject.get("content").getAsString());
        } else {
            throw new JsonParseException(ERROR_JSON_CONTAINS_MEMBER);
        }

        if (jsonObject.has("bankInfoTypeId")) {
            newsContent.setBankInfoTypeId(jsonObject.get("bankInfoTypeId").getAsInt());
        } else {
            throw new JsonParseException(ERROR_JSON_CONTAINS_MEMBER);
        }


        if (jsonObject.has("typeId")) {
            newsContent.setTypeId(jsonObject.get("typeId").getAsString());
        } else {
            throw new JsonParseException(ERROR_JSON_CONTAINS_MEMBER);
        }
        return newsContent;
    }
}
