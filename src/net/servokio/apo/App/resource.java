package net.servokio.apo.App;

import net.servokio.apo.main;

import java.util.ListResourceBundle;

public class resource extends ListResourceBundle {
    private static final Object[][] content = {
            {"key.author_image", main.bg.getString("author")}
    };
    public Object[][] getContents(){return content;}
}
