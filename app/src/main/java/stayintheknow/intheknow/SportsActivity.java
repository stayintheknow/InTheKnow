package stayintheknow.intheknow;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;


public class SportsActivity extends AppCompatActivity {

    private static final String NYTarticlesURL = "https://api.nytimes.com/svc/search/v2/articlesearch.json?fq=Sports&api-key=g7LVstKd7fsJilQAsdfgjInDmXRSco54";
    List<Articles> articles;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_article);
        RecyclerView rvArticles=findViewById(R.id.rvNewsfeed);
        articles=new ArrayList<>();

        final Adapter adapter=new Adapter(this,articles);

        rvArticles.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        rvArticles.setAdapter(adapter);

        AsyncHttpClient client  = new AsyncHttpClient();
        client.get(NYTarticlesURL, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray articlesJsonArray = response.getJSONArray("results");
                    articles.addAll((Articles.fromJsonArray(articlesJsonArray)));
                    adapter.notifyDataSetChanged();
                    //Log.d("Got the Array!", articlesJsonArray.toString());
                    Log.d("hey8", articles.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("Sad",articles.toString());

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });


    }
}
