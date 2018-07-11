package something.ru.newsreader.model.api.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NewsContentResponse {
    @SerializedName("title")
    @Expose
    private NewsTitle title;
    @SerializedName("creationDate")
    @Expose
    private PublicationDate creationDate;
    @SerializedName("lastModificationDate")
    @Expose
    private PublicationDate lastModificationDate;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("bankInfoTypeId")
    @Expose
    private Integer bankInfoTypeId;
    @SerializedName("typeId")
    @Expose
    private String typeId;

    public NewsTitle getTitle() {
        return title;
    }

    public PublicationDate getCreationDate() {
        return creationDate;
    }

    public PublicationDate getLastModificationDate() {
        return lastModificationDate;
    }

    public String getContent() {
        return content;
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
}
