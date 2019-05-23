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
import com.like.OnLikeListener;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;
import com.like.LikeButton;

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

        holder.saveButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                Log.d(TAG, "Saved: " + article.getTitle());
                Toast.makeText(context, "saved", Toast.LENGTH_SHORT).show();

                // UPDATE LIKES IN PARSE
                updateLikes(true, article);
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                Log.d(TAG, "Unsaved: " + article.getTitle());
                Toast.makeText(context, "no longer saved", Toast.LENGTH_SHORT).show();

                //TODO: UPDATE PARSE
                updateLikes(false, article);
            }
        });
    }

    private void updateLikes(boolean saved, Article article) {
        if(saved) {
            int likes = article.getLikeCount();
            article.setLikeCount(++likes);
            article.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if(e != null) {
                        Log.d(TAG, "done: Error while saving likes");
                        Toast.makeText(context, "Error while recording save", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                        return;
                    }
                    Log.d(TAG, "done: Successfully saved likes");
                    Toast.makeText(context, "article save recorded", Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                }
            });
            Log.d(TAG, "updateLikes: creating like object");
            Like like = new Like();
            Log.d(TAG, "updateLikes: created like object");
            like.setUser(ParseUser.getCurrentUser());
            Log.d(TAG, "updateLikes: set like user");
            like.setArticle(article);
            Log.d(TAG, "updateLikes: going to save like object in background");
            like.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if(e != null) {
                        Log.d(TAG, "done: Error while creating like object for user and article");
                        Toast.makeText(context, "Error while associating article with your account", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                        return;
                    }
                    Log.d(TAG, "done: created like object for user and article");
                    Toast.makeText(context, "article saved to your account", Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                }
            });
        } else {
            int likes = article.getLikeCount();
            article.setLikeCount(--likes);
            article.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if(e != null) {
                        Log.d(TAG, "done: Error while saving unlikes");
                        Toast.makeText(context, "Error while unsaving", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                        return;
                    }
                    Log.d(TAG, "done: Successfully saved unlike");
                    Toast.makeText(context, "Unsaved", Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                }
            });
            deleteLike(article);
        }
    }

    private void deleteLike(Article article) {
        ParseQuery<Like> query = new ParseQuery<Like>(Like.class);
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.whereEqualTo("article", article);
        query.findInBackground(new FindCallback<Like>() {
            @Override
            public void done(List<Like> likes, ParseException e) {
                if(e != null) {
                    Log.d(TAG, "Error: getting like object");
                    e.printStackTrace();
                    return;
                }
                Log.d(TAG, "Success: got like object");
                // Remove like
                ParseObject.deleteAllInBackground(likes, new DeleteCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e != null) {
                            Log.d(TAG, "Error: error deleting like object");
                            e.printStackTrace();
                            return;
                        }
                        Log.d(TAG, "Success: deleted like object");
                    }
                });
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void textToggle(final ViewHolder holder, final Article article) {
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
                gestureDetector.onTouchEvent(motionEvent);
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
        private TextView tvLikes;
        private ImageView ivImage;
        protected LinearLayout articleItem;
        protected ImageView ivHeart;
        protected ImageView ivHeartRed;
        protected ImageView ivComment;
        protected LikeButton saveButton;


        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvTimeStamp = itemView.findViewById(R.id.tvTimeStamp);
            articleItem = itemView.findViewById(R.id.articleItem);
//            ivHeart = itemView.findViewById(R.id.ivHeart);
//            ivHeartRed = itemView.findViewById(R.id.ivHeartRed);
            saveButton = itemView.findViewById(R.id.saveBtn);
            ivComment = itemView.findViewById(R.id.ivComment);
            tvLikes = itemView.findViewById(R.id.tvLikes);
//            bottomNavigationView = itemView.findViewById(R.id.bottom_navigation);
        }

        public void bind(Article article) {
            tvTitle.setText(article.getTitle());
            tvDescription.setText(article.getDescription());
            tvAuthor.setText(article.getAuthor().getName());
            /*SET DATE*/
            Date created = article.getTimeCreatedAt();
            String pattern = "MMMM dd, yyyy hh:mm a";
            SimpleDateFormat format = new SimpleDateFormat(pattern);
            String dateCreated = format.format(created);
            tvTimeStamp.setText(dateCreated);

            /*Inflate feature image*/
            String imgURL = "https://www.nytimes.com/" + article.getImageURL();

            /*SET FEATURE IMAGE*/
            ParseFile image = article.getImage();
            if(image != null) {
                Glide.with(context).load(image.getUrl()).into(ivImage);
            } else {
                Picasso.get().load(imgURL).resize(940, 688).into(ivImage);
            }
            /*SET LIKES*/
            if(article.getLikeCount() == 1){
                tvLikes.setText(article.getLikeCount() + " save");
            } else {
                tvLikes.setText(article.getLikeCount() + " saves");
            }
            /*SET LIKES*/
            if(article.getLikeCount() == 1){
                tvLikes.setText(article.getLikeCount() + " like");
            } else {
                tvLikes.setText(article.getLikeCount() + " likes");
            }
        }

    }

}