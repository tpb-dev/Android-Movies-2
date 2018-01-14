package club.thatpetbff.android_movies_2;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private MoviesAdapter mAdapter;
    int popularity = 0;
    Context mContext;

    OkHttpClient client = new OkHttpClient();

    private static final String API_KEY = BuildConfig.API_KEY;

    public String urlPopular= "https://api.themoviedb.org/3/movie/popular?api_key=" + API_KEY;

    public String urlTopRated= "https://api.themoviedb.org/3/movie/top_rated?api_key=" + API_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        mContext = this;
        mAdapter = new MoviesAdapter(this, new MoviesAdapter.OnItemClickListener() {
            @Override public void onItemClick(Movie item) {
                //Toast.makeText(mContext, "Item Clicked - " + item.getTitle(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(mContext, DetailActivity.class);
                String message = item.getTitle();
                intent.putExtra("MyClass", item);
                startActivity(intent);

            }
        });
        mRecyclerView.setAdapter(mAdapter);

        OkHttpHandler okHttpHandler= new OkHttpHandler();
        okHttpHandler.execute(urlPopular);

        popularity = 0;


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popularity = (popularity + 1) % 3;
                String[] txt = {"Sorted by Popularited", "Sorted by Ratings", "Sorted by Favorites"};
                Snackbar.make(view, txt[popularity], Snackbar.LENGTH_LONG).show();
                sortArray();
            }
        });
    }



    public void sortArray() {

        System.out.println("Reached sort: popularity: " + popularity);
        List<Movie> temp = mAdapter.getmMovieList();
        System.out.println(temp);
        if(popularity == 0) {
            OkHttpHandler okHttpHandler = new OkHttpHandler();
            okHttpHandler.execute(urlPopular);
        } else if(popularity == 1) {
            OkHttpHandler okHttpHandler = new OkHttpHandler();
            okHttpHandler.execute(urlTopRated);
        } else {
           GetFavoritesHandler handler = new GetFavoritesHandler(null);
           handler.execute();
        }



    }

    private class OkHttpHandler extends AsyncTask {

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
                Snackbar.make(mRecyclerView, "No internet connection detected", Snackbar.LENGTH_LONG).show();
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

    public void processJSON(String json) {
        System.out.println(json);
        Gson gson = new Gson();
        MovieResponse obj = gson.fromJson(json, MovieResponse.class);
        if(obj != null) {
            for (Movie a : obj.getResults()) {
                System.out.println("Movie: " + a.getTitle());
            }
            mAdapter = new MoviesAdapter(this, new MoviesAdapter.OnItemClickListener() {
                @Override public void onItemClick(Movie item) {
                    //Toast.makeText(mContext, "Item Clicked - " + item.getTitle(), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(mContext, DetailActivity.class);
                    String message = item.getTitle();
                    intent.putExtra("MyClass", item);
                    startActivity(intent);

                }
            });
            mAdapter.setMovieList(obj.getResults());
            mRecyclerView.setAdapter(mAdapter);
            System.out.println("Set the list to adapter");
        } else {
            Snackbar.make(mRecyclerView, "No internet connection detected", Snackbar.LENGTH_LONG).show();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class GetFavoritesHandler extends AsyncTask {

        public GetFavoritesHandler(Context fContext) {

        }

        @Override
        protected Cursor doInBackground(Object... params) {

            String[] projection = {
                    MyContract.MovieEntry.COLUMN_MOVIE_ID,
                    MyContract.MovieEntry.COLUMN_MOVIE_JSON
            };

            Cursor cur = mContext.getContentResolver().query(
                    MyContract.MovieEntry.CONTENT_URI,
                    projection,
                    null,
                    null,
                    null
            );
            return cur;
        }

        @Override
        protected void onPostExecute(Object movies) {
            processFavoriteMovies((Cursor)movies);
        }
    }

    private void processFavoriteMovies(Cursor c) {

        List<Movie> movies = new ArrayList<>();

        Gson gson = new Gson();

        while(c.moveToNext()){
            Integer movieID = Integer.parseInt(c.getString(c.getColumnIndexOrThrow(MyContract.MovieEntry.COLUMN_MOVIE_ID)));
            String movieJSON = c.getString(c.getColumnIndexOrThrow(MyContract.MovieEntry.COLUMN_MOVIE_JSON));

            Movie movie = gson.fromJson(movieJSON, Movie.class);
            movies.add(movie);
        }
        c.close();

        if (movies != null) {
            if (mAdapter != null) {
                mAdapter.setMovieList(movies);
            } else {
                mAdapter = new MoviesAdapter(this, new MoviesAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Movie item) {
                        //Toast.makeText(mContext, "Item Clicked - " + item.getTitle(), Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(mContext, DetailActivity.class);
                        String message = item.getTitle();
                        intent.putExtra("MyClass", item);
                        startActivity(intent);

                    }
                });
            }
            mRecyclerView.setAdapter(mAdapter);
        }
    }
}
