package club.thatpetbff.android_movies_2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by randalltom on 1/3/18.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder>  {



    public interface OnItemClickListener {
        void onItemClick(Movie movie);
    }

    private final OnItemClickListener listener;

    public List<Movie> getmMovieList() {
        return mMovieList;
    }

    public void setmMovieList(List<Movie> mMovieList) {
        this.mMovieList = mMovieList;
    }

    public LayoutInflater getmInflater() {
        return mInflater;
    }

    public void setmInflater(LayoutInflater mInflater) {
        this.mInflater = mInflater;
    }

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    private List<Movie> mMovieList;
    private LayoutInflater mInflater;
    private Context mContext;

    public MoviesAdapter(Context context, OnItemClickListener listener)
    {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mMovieList = new ArrayList<>();
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = mInflater.inflate(R.layout.row_movie, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MoviesAdapter.ViewHolder holder, int position)
    {
        Movie movie = mMovieList.get(position);
        holder.bind(movie, listener);

        // This is how we use Picasso to load images from the internet.

        if(Common.getInstance().getFavorite() == true) {
            Bitmap bitmap = Common.getInstance().getImages().get(movie.getId());

            // Set ImageView image as a Bitmap
            holder.imageView.setImageBitmap(bitmap);
        } else {
            Picasso.with(mContext)
                    .load("http://image.tmdb.org/t/p/w185/" + movie.getPoster())
                    .into(holder.imageView);

            BitmapHttpHandler bm = new BitmapHttpHandler();
            bm.execute("http://image.tmdb.org/t/p/w185/" + movie.getPoster(), movie.getId());

            System.out.println("Tried Picasso = " + position + ", " + movie.getPoster());
        }
    }

    private void setBitmapHash(Bitmap sh, Integer id) {
        Common.getInstance().getImages().put(id, sh);

    }

    private class BitmapHttpHandler extends AsyncTask {

        //OkHttpClient client = new OkHttpClient();

        /*
        public boolean isOnline() {
            ConnectivityManager cm =
                    (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnectedOrConnecting();
        } */

        @Override
        protected Bitmap doInBackground(Object... params) {

            try {
                URL url = new URL((String)params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                Common.getInstance().getImages().put((Integer)params[1], myBitmap);
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(Object s) {
            super.onPostExecute(s);
        }
    }

    @Override
    public int getItemCount()
    {
        return (mMovieList == null) ? 0 : mMovieList.size();
    }

    public void setMovieList(List<Movie> movieList)
    {
        this.mMovieList.clear();
        this.mMovieList.addAll(movieList);
        // The adapter needs to know that the data has changed. If we don't call this, app will crash.
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;

        private ImageView imageView;

        public ViewHolder(View itemView)
        {
            super(itemView);
            //name = (TextView) itemView.findViewById(R.id.name);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
        }


        public void bind(final Movie item, final OnItemClickListener listener) {
            //name.setText(item.getTitle());
            Picasso.with(itemView.getContext()).load(item.getPosterPath()).into(imageView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }

    }
}
