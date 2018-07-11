package something.ru.newsreader.model.database.entity;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

public class News extends RealmObject {
    @PrimaryKey
    private String id;
    @Index
    private String newsId;
    private String name;
    private String text;
    private Long publicationDate;
    private Integer bankInfoTypeId;

    public String getId() {
        return id;
    }

    public String getNewsId() {
        return newsId;
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }

    public Long getPublicationDate() {
        return publicationDate;
    }

    public Integer getBankInfoTypeId() {
        return bankInfoTypeId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setPublicationDate(Long publicationDate) {
        this.publicationDate = publicationDate;
    }

    public void setBankInfoTypeId(Integer bankInfoTypeId) {
        this.bankInfoTypeId = bankInfoTypeId;
    }
}
