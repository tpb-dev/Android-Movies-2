package club.thatpetbff.android_movies_2;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DetailActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TrailerAdapter adapter;
    private RecyclerView reviewRecyclerView;
    private ReviewAdapter reviewAdapter;
    Context context;
    Movie cMovie;

    OkHttpClient client = new OkHttpClient();

    private static final String API_KEY = BuildConfig.API_KEY;

     public String urlTrailer= "https://api.themoviedb.org/3/movie/%s/videos?api_key=" + API_KEY;

    public String urlReview= "https://api.themoviedb.org/3/movie/%s/reviews?api_key=" + API_KEY;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Movie movie = (Movie) getIntent().getSerializableExtra("MyClass");

        cMovie = movie;

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
//        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        // Capture the layout's TextView and set the string as its text
        TextView userRating = findViewById(R.id.userRating);
        userRating.setText("User Rating : " + movie.getVoteAverage());

        TextView releaseDate = findViewById(R.id.releaseDate);
        releaseDate.setText("Release Date : " + movie.getReleaseDate());

        TextView originalTitle = findViewById(R.id.originalTitle);
        originalTitle.setText("Original Title : " + movie.getOriginalTitle());

        TextView overview = findViewById(R.id.overview);
        overview.setText("Synposis : " + movie.getOverview());

        ImageView thumbnail = findViewById(R.id.thumbnail);

        Picasso.with(this).load("http://image.tmdb.org/t/p/w185/" + movie.getPoster()).into(thumbnail);

        System.out.println("Movie Mopvie = " + movie.getTitle());

        recyclerView = (RecyclerView) findViewById(R.id.detailRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        //mAdapter = new MoviesAdapter(this);
        //mRecyclerView.setAdapter(mAdapter);

        context = this;
        adapter = new TrailerAdapter(this, new TrailerAdapter.OnItemClickListener() {
            @Override public void onItemClick(Trailer item) {
                //Toast.makeText(mContext, "Item Clicked - " + item.getTitle(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(context, DetailActivity.class);
                String message = item.getName();
                //intent.putExtra("MyClass", item);
                startActivity(intent);

            }
        });
        recyclerView.setAdapter(adapter);

        NetworkHandler okHttpHandler= new NetworkHandler();
        urlTrailer = String.format(urlTrailer, movie.getId());
        okHttpHandler.execute(urlTrailer);

        reviewRecyclerView = (RecyclerView) findViewById(R.id.reviewRecyclerView);
        reviewRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        //mAdapter = new MoviesAdapter(this);
        //mRecyclerView.setAdapter(mAdapter);

        context = this;

        reviewAdapter = new ReviewAdapter(this, new ReviewAdapter.OnItemClickListener() {
            @Override public void onItemClick(Review item) {
                //Toast.makeText(mContext, "Item Clicked - " + item.getTitle(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(context, DetailActivity.class);
                String message = item.getContent();
                //intent.putExtra("MyClass", item);
                startActivity(intent);

            }
        });
        reviewRecyclerView.setAdapter(reviewAdapter);


        ReviewNetworkHandler reviewOkHttpHandler= new ReviewNetworkHandler();
        urlReview = String.format(urlReview, movie.getId());
        reviewOkHttpHandler.execute(urlReview);

    }

    public void onStarClick(View v) {
        switch (v.getId()) {
            case R.id.addToFavoriteButton:
                try {
                    System.out.println("Oppppooooossssuuttte");
                    Cursor c =
                            context.getContentResolver().query(MyContract.MovieEntry.CONTENT_URI,
                                    new String[]{MyContract.MovieEntry.COLUMN_MOVIE_ID},
                                    MyContract.MovieEntry.COLUMN_MOVIE_ID + " = " + cMovie.getId(),
                                    null,
                                    null);
                    if (c == null || c.getCount() == 0){
                        insertData(cMovie.getId());
                    }

                } catch (Exception e) {

                    e.printStackTrace();
                }
                break;
        }
    }

    public void insertData(Integer id) {

        ContentValues cv = new ContentValues();
        Gson gson = new Gson();

        String json = gson.toJson(cMovie);

        cv.put(MyContract.MovieEntry.COLUMN_MOVIE_ID, id);
        cv.put(MyContract.MovieEntry.COLUMN_MOVIE_JSON, json);

        context.getContentResolver().insert(MyContract.MovieEntry.CONTENT_URI,
                cv);
    }



    private class NetworkHandler extends AsyncTask {

        OkHttpClient client = new OkHttpClient();

        public boolean isOnline() {
            ConnectivityManager cm =
                    (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnectedOrConnecting();
        }

        @Override
        protected String doInBackground(Object... params) {

            Request.Builder builder = new Request.Builder();
            builder.url((String)params[0]);
            Request request = builder.build();

            if(isOnline()) {

                try {
                    Response response = client.newCall(request).execute();
                    return response.body().string();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Snackbar.make(recyclerView, "No internet connection detected", Snackbar.LENGTH_LONG).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object s) {
            String a = (String)s;
            super.onPostExecute(a);
            processJSON(a);
        }

    }

    private class ReviewNetworkHandler extends AsyncTask {

        OkHttpClient client = new OkHttpClient();

        public boolean isOnline() {
            ConnectivityManager cm =
                    (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnectedOrConnecting();
        }

        @Override
        protected String doInBackground(Object... params) {

            Request.Builder builder = new Request.Builder();
            builder.url((String)params[0]);
            Request request = builder.build();

            if(isOnline()) {

                try {
                    Response response = client.newCall(request).execute();
                    return response.body().string();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Snackbar.make(recyclerView, "No internet connection detected", Snackbar.LENGTH_LONG).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object s) {
            String a = (String)s;
            super.onPostExecute(a);
            processReviewJSON(a);
        }

    }

    public void processJSON(String json) {
        System.out.println(json);
        Gson gson = new Gson();
        TrailerResponse obj = gson.fromJson(json, TrailerResponse.class);
        if(obj != null) {
            for (Trailer a : obj.getResults()) {
                System.out.println("Movie: " + a.getName());
            }
            adapter = new TrailerAdapter(this, new TrailerAdapter.OnItemClickListener() {
                @Override public void onItemClick(Trailer item) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + item.getKey()));
                    startActivity(browserIntent);
                }
            });
            adapter.setTrailerList(obj.getResults());
            recyclerView.setAdapter(adapter);
            System.out.println("Set the list to adapter");
        } else {
            //Snackbar.make(recyclerView, "No internet connection detected", Snackbar.LENGTH_LONG).show();
        }
    }

    public void processReviewJSON(String json) {
        System.out.println(json);
        Gson gson = new Gson();
        ReviewResponse obj = gson.fromJson(json, ReviewResponse.class);
        if(obj != null) {
            for (Review a : obj.getResults()) {
                System.out.println("Movie: " + a.getContent());
            }
            reviewAdapter = new ReviewAdapter(this, new ReviewAdapter.OnItemClickListener() {
                @Override public void onItemClick(Review item) {

                }
            });
            reviewAdapter.setReviewList(obj.getResults());
            reviewRecyclerView.setAdapter(reviewAdapter);
            System.out.println("Set the list to adapter");
        } else {
            //Snackbar.make(recyclerView, "No internet connection detected", Snackbar.LENGTH_LONG).show();
        }
    }
}
