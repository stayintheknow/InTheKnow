package stayintheknow.intheknow.utils.APIs;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import stayintheknow.intheknow.Article;
import stayintheknow.intheknow.Author;

/**
 * This class makes api calls to New York Times and saves the corresponding articles in the parse API
 **/

public class NYTArticleAPI {

    private static final String TAG = "NYTArticleAPI";

    public static boolean getNYTArticles() {
        //TODO: MAKE API CALL TO NYTIMES WITH SOURCE PARAMATER = nyt (wrap in try catch blocks, return false if error caught)
        //TODO: EACH ARTICLE RETRIEVED IS SAVED TO PARSE DATABASE
        //TODO: USE FROMJSONARRAY() TO ACHIEVE THIS
        //TODO: if toParse() returns false, return false at that time
        //TODO: look at Article.java to attributes about article needed
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
        String author, url, description, picture_url, title = "";

        try {
            author = jsonObject.getJSONObject("byline").getString("original");
            url = jsonObject.getString("web_url");
            description = jsonObject.getString("snippet");
            //if (array.size() < 0) dont add it to the arrayList
            picture_url = jsonObject.getJSONArray("multimedia").getJSONObject(0).getString("url");
            title = jsonObject.getJSONObject("headline").getString("main");
            //TODO: GET ARTICLE CATEGORY AND CREATED DATE
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
        //TODO: article.setCategory();
        article.setDataSouce("New York Times");
        article.setDescription(description);
        //TODO: make https request to get image and save to a java File object
        //todo: File pictureFile = ...
        //todo: ParseFile image = new ParseFile(pictureFile);
        //todo: article.setImage();

        return true;
    }
}
