package stayintheknow.intheknow;

//import android.annotation.SuppressLint;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import stayintheknow.intheknow.utils.Heart;

public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ViewHolder>{
    private static final String TAG = "ArticlesAdapter";

    private Context context;
    private List<Article> articles;

    private GestureDetector gestureDetector;
    private Heart heart;


    public ArticlesAdapter(Context context, List<Article> articles) {
        this.context = context;
        this.articles = articles;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Creates row in our recycler view
        View view = LayoutInflater.from(context).inflate(R.layout.item_article, parent, false);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Binds data at given position into the view holder
        final Article article = articles.get(position);
        holder.bind(article);

        /*Go to article on image click*/
        holder.ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Article " + article.getTitle() + " has been clicked");
                Toast.makeText(context, "Opening '" + article.getTitle() + "'", Toast.LENGTH_SHORT).show();
                goToWebView(article.getURL());
            }
        });
        /*Go to article on title click*/
        holder.tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Article " + article.getTitle() + " has been clicked");
                Toast.makeText(context, "Opening '" + article.getTitle() + "'", Toast.LENGTH_SHORT).show();
                goToWebView(article.getURL());
            }
        });

        /*Like and Comment on Article Functionality*/
        holder.ivHeartRed.setVisibility(View.GONE);
        holder.ivHeart.setVisibility(View.VISIBLE);
        heart = new Heart(holder.ivHeart, holder.ivHeartRed);
        gestureDetector = new GestureDetector(context, new GestureListener());
        textToggle(holder);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void textToggle(ViewHolder holder) {
        holder.ivHeartRed.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Log.d(TAG, "onTouch: red heart touch detected");
                return gestureDetector.onTouchEvent(motionEvent);
            }
        });

        holder.ivHeart.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Log.d(TAG, "onTouch: heart touch detected");
                return gestureDetector.onTouchEvent(motionEvent);
            }
        });
    }

    private void goToWebView(String url) {
        Log.d(TAG, "Going to article webview");
        Intent i = new Intent(context, ArticleWebView.class);
        i.putExtra("url", url);
        context.startActivity(i);
    }

    @Override
    public int getItemCount() {
        // Returns how many items are in our data set
        return articles.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        articles.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Article> list) {
        articles.addAll(list);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle;
        private TextView tvDescription;
        private TextView tvAuthor;
        private TextView tvTimeStamp;
        private ImageView ivImage;
        protected LinearLayout articleItem;
        protected ImageView ivHeart;
        protected ImageView ivHeartRed;
        protected ImageView ivComment;


        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvTimeStamp = itemView.findViewById(R.id.tvTimeStamp);
            articleItem = itemView.findViewById(R.id.articleItem);
            ivHeart = itemView.findViewById(R.id.ivHeart);
            ivHeartRed = itemView.findViewById(R.id.ivHeartRed);
            ivComment = itemView.findViewById(R.id.ivComment);
//            bottomNavigationView = itemView.findViewById(R.id.bottom_navigation);
        }

        public void bind(Article article) {
            tvTitle.setText(article.getTitle());
            tvDescription.setText(article.getDescription());
            tvAuthor.setText(article.getAuthor().getName());

            /*Date to string*/
            Date created = article.getTimeCreatedAt();
            String pattern = "MMMM dd, yyyy hh:mm a";
            SimpleDateFormat format = new SimpleDateFormat(pattern);
            String dateCreated = format.format(created);
            tvTimeStamp.setText(dateCreated);

            /*Inflate feature image*/
            String imgURL = "https://www.nytimes.com/" + article.getImageURL();
            ParseFile image = article.getImage();
            if(image != null) {
                Glide.with(context).load(image.getUrl()).into(ivImage);
            } else {
                Picasso.get().load(imgURL).resize(940, 688).into(ivImage);
            }
        }

        public void onClick(View view) {
            Log.d(TAG, "onClick: " + getPosition() + " " + tvTitle);
        }
    }

    /*Where we quesry our database to add or remove likes on an article*/
    public class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Log.d(TAG, "onDoubleTap: double tap detected");
            heart.toggleLike();
            return true;
        }
    }

}