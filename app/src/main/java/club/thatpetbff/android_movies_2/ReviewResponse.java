package club.thatpetbff.android_movies_2;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by randalltom on 1/12/18.
 */

public class ReviewResponse {
    @SerializedName("id")
    Integer id;

    @SerializedName("page")
    Integer page;

    @SerializedName("results")
    ArrayList<Review> results;

    @SerializedName("total_pages")
    Integer totalPages;

    @SerializedName("total_results")
    Integer totalResults;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public ArrayList<Review> getResults() {
        return results;
    }

    public void setResults(ArrayList<Review> results) {
        this.results = results;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }
}
