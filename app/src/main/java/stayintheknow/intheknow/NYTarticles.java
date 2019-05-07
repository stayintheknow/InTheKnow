package stayintheknow.intheknow;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

//@Parcel
public class NYTarticles {

    String web_url;
    String snippet;
    String lead_paragraph;
//    String overview;
//    double vote;
//    String backdroppath;


    //using parceler
    public NYTarticles(){

    }


    public NYTarticles(JSONObject jsonObject) throws JSONException {

        web_url = jsonObject.getString("web_url");
        snippet= jsonObject.getString("snippet");
        lead_paragraph = jsonObject.getString("lead_paragraph");

    }
    public static List<NYTarticles> fromJsonArray(JSONArray articleJsonArray) throws JSONException{
        List<NYTarticles> articles = new ArrayList<>();
        for(int i=0; i < articleJsonArray.length(); i++){
            articles.add(new NYTarticles(articleJsonArray.getJSONObject(i)));
        }

        return articles;
    }

    public String getURL() {
        return web_url;
    }

    public String getSnippet() {
        return snippet;
    }

    public String getLeadParagraph() {
        return lead_paragraph;
    }

}