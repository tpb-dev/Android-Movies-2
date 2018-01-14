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

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    public interface OnItemClickListener {
        void onItemClick(Review review);
    }

    private final OnItemClickListener listener;

    private List<Review> reviewList;

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

    public ReviewAdapter(Context context, OnItemClickListener listener)
    {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.reviewList = new ArrayList<>();
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = inflater.inflate(R.layout.row_review, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ReviewAdapter.ViewHolder holder, int position)
    {
        Review review = reviewList.get(position);
        holder.bind(review, listener);

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
        return (reviewList == null) ? 0 : reviewList.size();
    }

    public void setReviewList(List<Review> reviewList)
    {
        this.reviewList.clear();
        this.reviewList.addAll(reviewList);
        // The adapter needs to know that the data has changed. If we don't call this, app will crash.
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView reviewView;

        public ViewHolder(View itemView)
        {
            super(itemView);
            reviewView = (TextView) itemView.findViewById(R.id.reviewView);
            //imageView = (ImageView) itemView.findViewById(R.id.imageView);
        }


        public void bind(final Review item, final OnItemClickListener listener) {
            reviewView.setText("Review: " + item.getContent().trim());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }

    }

}