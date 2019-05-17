package stayintheknow.intheknow.utils.APIs;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import stayintheknow.intheknow.Article;
import stayintheknow.intheknow.Author;

/**
 * This class makes api calls to New York Times and saves the corresponding articles in the parse API
 **/


public class NYTArticleAPI {

    private static String[] sections = {"U.S" , "Health" , "NewYork" , "Tech" , "World"};


    private static List<NYTArticleAPI> articles;

    private static final String TAG = "NYTArticleAPI";


    public static void getAllSections(){

        for ( int i = 0; i < sections.length; i++){
            getNYTArticles(sections[i]);
        }

    }

    public static boolean getNYTArticles(String section) {

       String ARTICLE_URL = "https://api.nytimes.com/svc/search/v2/articlesearch.json?fq="+ section +"&api-key=g7LVstKd7fsJilQAsdfgjInDmXRSco54";

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(ARTICLE_URL, new JsonHttpResponseHandler() { //<-- callback method to the API

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    JSONObject results = response.getJSONObject("response");
                    JSONArray articlesArray = results.getJSONArray("docs");
                    articles =  fromJsonArray(articlesArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d(TAG, "getNYTArticles: ");
                }
                //TODO: find a way to return false on an overwritten void method

            }

        });

        return true;
    }

    private static List<NYTArticleAPI> fromJsonArray(JSONArray articlesJsonArray) throws JSONException {
        List<NYTArticleAPI> articles = new ArrayList<>();
        for ( int i = 0; i < articlesJsonArray.length(); i++){
            if(articlesJsonArray.getJSONObject(i).getJSONArray("multimedia").length() > 0) {
                toParse(articlesJsonArray.getJSONObject(i));
                articles.add(new NYTArticleAPI());
            }
        }
        return articles;
    }

    private static boolean toParse(JSONObject jsonObject) {
        String author, url, description, picture_url, title, category, publication_date;

        try {
            author = jsonObject.getJSONObject("byline").getString("original");
            url = jsonObject.getString("web_url");
            description = jsonObject.getString("snippet");
            //if (array.size() < 0) dont add it to the arrayList
            picture_url = jsonObject.getJSONArray("multimedia").getJSONObject(0).getString("url");
            title = jsonObject.getJSONObject("headline").getString("main");
            category = jsonObject.getString("section_name");
            publication_date = jsonObject.getString("pub_date");

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "toParse: ");
            return false;
        }

        /*Add author to parse*/
        Author auth = new Author();
        auth.setName(author);
        /*Add article to parse*/
        Article article = new Article();
        article.setTitle(title);
        article.setAuthor(auth);
        article.setURL(url);
        article.setCategory(category);
        article.setDataSouce("New York Times");
        article.setDescription(description);
        //TODO: set publication date
        //TODO: make https request to get image and save to a java File object
        //todo: File pictureFile = ...
        //todo: ParseFile image = new ParseFile(pictureFile);
        //todo: article.setImage();

        return true;
    }
}
