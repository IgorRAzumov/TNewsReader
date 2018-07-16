package something.ru.newsreader.model.api.deserializers;

import java.util.Date;

public class DeserializerUtils {
    public static Date createDateFromLong(Long dateLong) {
        Date date;
        try {
            date = new Date(dateLong);
        } catch (Exception e) {
            date = new Date();
        }
        return date;
    }
}
