package stayintheknow.intheknow;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

@ParseClassName("Author")
public class Author extends ParseObject {

    public static final String KEY_NAME = "name";
    public static final String KEY_IMAGE = "image";

    public String getName() {
        return getString(KEY_NAME);
    }

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public void setName(String name) {
        put(KEY_NAME, name);
    }

    public void setImage(ParseFile image) {
        put(KEY_IMAGE, image);
    }
}
