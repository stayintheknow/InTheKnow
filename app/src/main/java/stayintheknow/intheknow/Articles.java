package stayintheknow.intheknow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Articles {
    String picture_url;
    String auhor;
    String url;
    String decription;
    String title;

    public Articles(JSONObject jsonObject) throws JSONException {
        auhor = jsonObject.getJSONObject("byline").getString("original");
        url = jsonObject.getString("web_url");
        decription = jsonObject.getString("snippet");
        //if (array.size() < 0) dont add it to the arrayList
        picture_url = jsonObject.getJSONArray("multimedia").getJSONObject(0).getString("url");
        title = jsonObject.getJSONObject("headline").getString("main");
    }


    public static List<Articles> fromJsonArray(JSONArray articlesJsonArray) throws JSONException {
        List<Articles> articles = new ArrayList<>();
        for ( int i = 0; i < articlesJsonArray.length(); i++){
            if(articlesJsonArray.getJSONObject(i).getJSONArray("multimedia").length() > 0) {
                articles.add(new Articles(articlesJsonArray.getJSONObject(i)));
            }
        }
        return articles;
    }

    public String getPicture_url() {

        return "https://www.nytimes.com/".concat(picture_url);
    }

    public String getAuhor() {
        return auhor;
    }

    public String getUrl() {
        return url;
    }

    public String getDescription() {
        return decription;
    }

    public String getTitle() {
        return title;
    }
}
