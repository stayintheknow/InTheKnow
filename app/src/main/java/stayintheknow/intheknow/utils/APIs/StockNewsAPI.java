package stayintheknow.intheknow.utils.APIs;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;
import stayintheknow.intheknow.Article;
import stayintheknow.intheknow.Author;


public class StackNewsAPI {

    private static String[] preferences = {"TSLA" , "FB" , "AMAZON" , "APPLE" , "TWITCH"};

    private static final String TAG = "StockNewsAPI";

    String numberItems = "15";
    String API_KEY = "tugyrqhkflh8yujmx7cik8ttujmhwwfej0e6qsvw"

    public static void getAllPreferences(){

        for ( int i = 0; i < preferences.length; i++){
            getStockTArticles(preferences[i]);
        }
    }

    public static void getStockTArticles(String preference) {

        String API_URL = "https://stocknewsapi.com/api/v1?tickers=" + preference + "&items=" + numberItems + "fallback=true&token=" + API_KEY;

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(ARTICLE_URL, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    Log.d(TAG, "onSuccess: json array of articles gotten");
                    JSONArray data = response.getJSONObject("data");
                    Log.d(TAG, "onSuccess: " + data);
                    fromJsonArray(data);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "getStockTArticles: problem reading JSON object");
                }

            }

        });

    }

    private static void fromJsonArray(JSONArray articlesJsonArray) throws JSONException {
        for ( int i = 0; i < articlesJsonArray.length(); i++){
            if(articlesJsonArray.getJSONObject(i).getString("image_url") =! null){
                toParse(articlesJsonArray.getJSONObject(i));
                Log.d(TAG, "fromJsonArray: article added to parse database");
            }

        }
    }

    private static boolean toParse(JSONObject jsonObject) {
        String author, url, description, picture_url, title, category, publication_date;

        try {
            //There was not author, tho there was a source name
//            author = ;
            author = jsonObject.getString("source_name");
            url = jsonObject.getString("news_url");
            description = jsonObject.getString("text");
            picture_url = jsonObject.getString("image_url");
            title = jsonObject.getString("title");
//            category = jsonObject.getString("section_name"); There was no category, tho we can use STOCKS or Finance
            category = "Finances";
            publication_date = jsonObject.getString("date");
            Log.d(TAG, "toParse: got article features successfully");

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "toParse: Json error whene reading strings");
            return false;
        }

        if(author.length() < 5 || author == null) {
            return false;
        }

        /*Save article*/
        saveArticle(author, url, description, picture_url, title, category, publication_date);

//        Log.d(TAG, "toParse: publication date: " + publication_date);
        //TODO: set publication date
        return true;
    }

    private static void saveArticle(final String author, final String url, final String description, final String picture_url, final String title, final String category, final String publication_date) {
        ParseQuery<Author> authorQuery = ParseQuery.getQuery(Author.class);
        authorQuery.whereEqualTo(Author.KEY_NAME, author);
        authorQuery.findInBackground(new FindCallback<Author>() {
            @Override
            public void done(List<Author> authors, ParseException e) {
                if(e != null) {
                    Log.e(TAG, "Error: " + e.getMessage() );
                    return;
                }
                if(authors.size() != 0 ){
                    Log.d(TAG, "Author Exists: " + author);
                    /*Save article*/
                    saveArticleAuth(authors.get(0), url, description, picture_url, title, category, publication_date);
                    return;
                }

                final Author auth = new Author();
                auth.setName(author);

                auth.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e != null) {
                            Log.d(TAG, "done: Error while saving author");
                            e.printStackTrace();
                            return;
                        }
                        Log.d(TAG, "done: Successfully saved author " + author);
                    }
                });


                /*save article*/
                saveArticleAuth(auth, url, description, picture_url, title, category, publication_date);
            }
        });
    }

    private static void saveArticleAuth(final Author author, final String url, final String description, final String picture_url, final String title, final String category, String publication_date) {
        /*Check if Article Exists*/
        ParseQuery<Article> articleQuery = ParseQuery.getQuery(Article.class);
        articleQuery.whereEqualTo(Article.KEY_TITLE, title);
        articleQuery.findInBackground(new FindCallback<Article>() {
            @Override
            public void done(List<Article> articleList, ParseException e) {
                if(e != null) {
                    Log.e(TAG, "Error: " + e.getMessage());
                    return;
                }
                if(articleList.size() != 0) {
                    Log.d(TAG, "done: This article exists already " + title);
                    return;
                }

                /*Save the new article*/
                //TODO: SET PUBLICATION DATE
                Article article = new Article();
                article.setTitle(title);
                article.setAuthor(author);
                article.setURL(url);
                article.setCategory(category);
                article.setDataSouce("New York Times");
                article.setDescription(description);
                article.setImageURL(picture_url);
                article.setLikeCount(0);


                article.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e != null) {
                            Log.d(TAG, "done: Error while saving article");
                            e.printStackTrace();
                            return;
                        }
                        Log.d(TAG, "done: Successfully saved article");
                    }
                });

            }
        });
    }

}
