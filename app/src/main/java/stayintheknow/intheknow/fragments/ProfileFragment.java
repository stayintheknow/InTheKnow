package stayintheknow.intheknow.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import stayintheknow.intheknow.Article;
import stayintheknow.intheknow.ArticlesAdapter;
import stayintheknow.intheknow.Like;
import stayintheknow.intheknow.R;

public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";

    private RecyclerView rvProfilefeed;
    private ArticlesAdapter adapter;
    private List<Like> mLikes;
    private List<Article> mArticles;

    private TextView tvUsername;
    private TextView tvName;
    private TextView tvBio;
    private CircleImageView profile_image;

    private ParseUser user;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        tvUsername = view.findViewById(R.id.tvUsername);
        tvName = view.findViewById(R.id.tvName);
        tvBio = view.findViewById(R.id.tvBio);
        profile_image = view.findViewById(R.id.profile_image);

        user = ParseUser.getCurrentUser();

        /*Set Profile Data*/
        tvUsername.setText("--" + user.getUsername() + "--");
        tvName.setText(user.getString("name"));
        tvBio.setText(user.getString("bio"));

        ParseFile image = user.getParseFile("profileImage");
        Log.d(TAG, "onViewCreated: img null " + (image == null));
        if(image != null) {
            Glide.with(getContext()).load(image.getUrl()).into(profile_image);
        }

        rvProfilefeed = view.findViewById(R.id.rvProfilefeed);

        // Create data source
        mArticles = new ArrayList<>();
        mLikes = new ArrayList<>();
        // Create adapter
        adapter = new ArticlesAdapter(getContext(), mArticles);
        // set the adapter on the recycler view
        rvProfilefeed.setAdapter(adapter);
        // set the layout manager on the recycler view
        rvProfilefeed.setLayoutManager(new LinearLayoutManager(getContext()));

        // Fill the data source
        queryArticles();
    }

    private void queryArticles() {
        ParseQuery<Like> likeQuery = new ParseQuery<Like>(Like.class);
        likeQuery.include(Like.KEY_ARTICLE);
        likeQuery.include(Like.KEY_USER);
        likeQuery.addDescendingOrder(Article.KEY_CREATED_AT);
        likeQuery.whereEqualTo(Like.KEY_USER, ParseUser.getCurrentUser());
        likeQuery.findInBackground(new FindCallback<Like>() {
            @Override
            public void done(List<Like> likes, ParseException e) {
                if(e != null) {
                    Log.e(TAG, "Error: trouble finding like objects");
                    e.printStackTrace();
                    return;
                }
                mLikes.addAll(likes);
                Log.d(TAG, "Success: Got all likes");
                for (Like like : mLikes) {
                    try {
                        like.getArticle().getAuthor().fetchIfNeeded();
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }
                    mArticles.add(like.getArticle());
                    Log.d(TAG, "Adding Article: " + like.getArticle().getTitle());
                }
                adapter.notifyDataSetChanged();
                for(int i = 0; i < likes.size(); i++) {
                    Article article = likes.get(i).getArticle();
                    Log.d(TAG, "Article: " + likes.get(i).getArticle().getTitle());
                    Log.d(TAG, "Time Created" + likes.get(i).getArticle().getTimeCreatedAt().toString());
                    if(article.getAuthor() == null )
                        Log.e(TAG, "Null author object");
                    else Log.d(TAG, "Author: " + likes.get(i).getArticle().getAuthor().getName() );
                }
            }
        });
    }
}
