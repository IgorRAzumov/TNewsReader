package something.ru.newsreader.model.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class News extends RealmObject {
    @PrimaryKey
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("publicationDate")
    @Expose
    private Long publicationDate;
    @SerializedName("bankInfoTypeId")
    @Expose
    private Integer bankInfoTypeId;

    public String getId() {
        return id;
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
