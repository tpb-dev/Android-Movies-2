package club.thatpetbff.android_movies_2;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by rtom on 1/11/18.
 */

public class Trailer {
    @SerializedName("id")
    String id;

    @SerializedName("iso_639_1")
    String iso639;

    @SerializedName("iso_3166_1")
    String iso3166;

    @SerializedName("key")
    String key;

    @SerializedName("name")
    String name;

    @SerializedName("site")
    String site;

    @SerializedName("size")
    Integer size;

    @SerializedName("type")
    String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIso639() {
        return iso639;
    }

    public void setIso639(String iso639) {
        this.iso639 = iso639;
    }

    public String getIso3166() {
        return iso3166;
    }

    public void setIso3166(String iso3166) {
        this.iso3166 = iso3166;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


}
