package club.thatpetbff.android_movies_2;

import android.graphics.Bitmap;

import java.util.HashMap;
import java.util.List;

/**
 * Created by rtom on 1/12/18.
 */

public class Common {
    private boolean isFavorite;
    private HashMap<Integer, Bitmap> images;

    private static Common instance = null;
    protected Common() {
        // Exists only to defeat instantiation.
    }
    public static Common getInstance() {
        if(instance == null) {
            instance = new Common();
        }
        return instance;
    }

    public boolean getFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public HashMap<Integer, Bitmap> getImages() {
        if(images == null) {
            images = new HashMap();
        }
        return images;
    }

    public void setImages(HashMap<Integer, Bitmap> images) {
        this.images = images;
    }
}
