package stayintheknow.intheknow.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import stayintheknow.intheknow.Article;
import stayintheknow.intheknow.ArticlesAdapter;
import stayintheknow.intheknow.R;
import stayintheknow.intheknow.utils.APIs.NYTArticleAPI;
import stayintheknow.intheknow.utils.Heart;

public class ScienceFragment extends Fragment {private SwipeRefreshLayout swipeContainer;


    private static final String TAG = "NewsfeedFragment";

    private RecyclerView rvNewsfeed;
    private ArticlesAdapter adapter;
    private List<Article> mArticles;

    private GestureDetector gestureDetector;
    private Heart heart;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_newsfeed, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        rvNewsfeed = view.findViewById(R.id.rvNewsfeed);

        // Create data source
        mArticles = new ArrayList<>();
        // Create adapter
        adapter = new ArticlesAdapter(getContext(), mArticles);
        // set the adapter on the recycler view
        rvNewsfeed.setAdapter(adapter);
        // set the layout manager on the recycler view
        rvNewsfeed.setLayoutManager(new LinearLayoutManager(getContext()));

        queryArticles();

        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchArticlesAsync();
            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

    }

    private void fetchArticlesAsync() {
        adapter.clear();
        queryArticles();
        swipeContainer.setRefreshing(false);
    }

    private void queryArticles() {

        /*Fetch from api*/
        NYTArticleAPI.getNYTArticles("Science");

        ParseQuery<Article> articleQuery = new ParseQuery<Article>(Article.class);
        articleQuery.include(Article.KEY_AUTHOR);
        articleQuery.include(Article.KEY_CREATED_AT);
        articleQuery.whereEqualTo(Article.KEY_CATEGORY, "Science");
        articleQuery.addDescendingOrder(Article.KEY_CREATED_AT);
        articleQuery.findInBackground(new FindCallback<Article>() {
            @Override
            public void done(List<Article> articles, ParseException e) {
                if( e != null ) {
                    Log.e(TAG, "Error with query");
                    e.printStackTrace();
                    return;
                }
                mArticles.addAll(articles);
                adapter.notifyDataSetChanged();
                for(int i = 0; i < articles.size(); i++) {
                    Article article = articles.get(i);
                    Log.d(TAG, "Article: " + article.getTitle());
                    Log.d(TAG, "Time Created" + article.getTimeCreatedAt().toString());
                    if(article.getAuthor() == null )
                        Log.e(TAG, "Null author object");
                    else Log.d(TAG, "Author: " + article.getAuthor().getName() );
                }
            }
        });
    }

}

