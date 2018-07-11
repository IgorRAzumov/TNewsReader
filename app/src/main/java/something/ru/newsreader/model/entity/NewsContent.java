package something.ru.newsreader.model.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class NewsContent extends RealmObject {
    @PrimaryKey
    @SerializedName("news")
    @Expose
    private String newsId;
    @SerializedName("creationDate")
    @Expose
    private Long creationDate;
    @SerializedName("lastModificationDate")
    @Expose
    private Long lastModificationDate;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("bankInfoTypeId")
    @Expose
    private Integer bankInfoTypeId;
    @SerializedName("typeId")
    @Expose
    private String typeId;

    public String getId() {
        return newsId;
    }

    public String getNewsId() {
        return newsId;
    }

    public Long getCreationDate() {
        return creationDate;
    }

    public Long getLastModificationDate() {
        return lastModificationDate;
    }

    public String getContent() {
        return content;
    }

    public void setId(String id) {
        this.newsId = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getBankInfoTypeId() {
        return bankInfoTypeId;
    }

    public void setBankInfoTypeId(Integer bankInfoTypeId) {
        this.bankInfoTypeId = bankInfoTypeId;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public void setLastModificationDate(Long lastModificationDate) {
        this.lastModificationDate = lastModificationDate;
    }

    public void setCreationDate(Long creationDate) {
        this.creationDate = creationDate;
    }
}
