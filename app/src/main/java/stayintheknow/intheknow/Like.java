package stayintheknow.intheknow;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Like")
public class Like extends ParseObject {
    public static final String KEY_USER = "user";
    public static final String KEY_ARTICLE = "article";

    public String getUser() {
        return getString(KEY_USER);
    }

    public Article getArticle() {
        return (Article) getParseObject(KEY_ARTICLE);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    public void setArticle(Article article) {
        put(KEY_ARTICLE, article);
    }
}
