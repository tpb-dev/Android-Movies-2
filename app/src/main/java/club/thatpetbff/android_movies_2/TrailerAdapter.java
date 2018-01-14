package club.thatpetbff.android_movies_2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rtom on 1/11/18.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {
    public interface OnItemClickListener {
        void onItemClick(Trailer trailer);
    }

    private final OnItemClickListener listener;

    private List<Trailer> trailerList;

    private LayoutInflater inflater;
    private Context context;

    public LayoutInflater getInflater() {
        return inflater;
    }

    public void setInflater(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public TrailerAdapter(Context context, OnItemClickListener listener)
    {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.trailerList = new ArrayList<>();
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = inflater.inflate(R.layout.row_trailer, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TrailerAdapter.ViewHolder holder, int position)
    {
        Trailer trailer = trailerList.get(position);
        holder.bind(trailer, listener);

        /*
        // This is how we use Picasso to load images from the internet.
        Picasso.with(mContext)
                .load("http://image.tmdb.org/t/p/w185/" + movie.getPoster())
                .into(holder.imageView);
        System.out.println("Tried Picasso = " + position + ", " + movie.getPoster());
        */
    }

    @Override
    public int getItemCount()
    {
        return (trailerList == null) ? 0 : trailerList.size();
    }

    public void setTrailerList(List<Trailer> trailerList)
    {
        this.trailerList.clear();
        this.trailerList.addAll(trailerList);
        // The adapter needs to know that the data has changed. If we don't call this, app will crash.
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public ViewHolder(View itemView)
        {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.textView);
            //imageView = (ImageView) itemView.findViewById(R.id.imageView);
        }


        public void bind(final Trailer item, final OnItemClickListener listener) {
            textView.setText("Trailer: " + item.getName());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }

    }

}