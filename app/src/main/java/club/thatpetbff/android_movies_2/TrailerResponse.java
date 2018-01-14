package club.thatpetbff.android_movies_2;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by rtom on 1/11/18.
 */

public class TrailerResponse {


    @SerializedName("id")
    Integer id;

    @SerializedName("results")
    ArrayList<Trailer> results;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ArrayList<Trailer> getResults() {
        return results;
    }

    public void setResults(ArrayList<Trailer> results) {
        this.results = results;
    }

}
