package stayintheknow.intheknow;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

@ParseClassName("Article")
public class Article extends ParseObject {

    public static final String KEY_AUTHOR = "author";
    public static final String KEY_URL = "url";
    public static final String KEY_TITLE = "title";
    public static final String KEY_CATEGORY = "category";
    public static final String KEY_LIKE_COUNT = "likeCount";
    public static final String KEY_IMAGE = "featureImage";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_DATA_SOURCE = "dataSource";

    public String getDataSource() {
        return getString(KEY_DATA_SOURCE);
    }

    public String getURL() {
        return getString(KEY_URL);
    }

    public Author getAuthor() {
        return (Author) getParseObject(KEY_AUTHOR);
    }

    public String getTitle() {
        return getString(KEY_TITLE);
    }

    public String getCategory() {
        return getString(KEY_CATEGORY);
    }

    public int getLikeCount() {
        return getInt(KEY_LIKE_COUNT);
    }

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public void setDataSouce(String dataSouce) {
        put(KEY_DATA_SOURCE, dataSouce);
    }

    public void setURL(String url) {
        put(KEY_URL, url);
    }

    public void setAuthor(Author author) {
        put(KEY_AUTHOR, author);
    }

    public void setTitle(String title) {
        put(KEY_TITLE, title);
    }

    public void setCategory(String category) {
        put(KEY_CATEGORY, category);
    }

    public void setLikeCount(int count) {
        put(KEY_LIKE_COUNT, count);
    }

    public void setImage(ParseFile image) {
        put(KEY_IMAGE, image);
    }

    public void setDescription(String description) {
        put(KEY_DESCRIPTION, description);
    }
}
