package ires.baylor.edu.logerrors.matcher;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.Document;
import java.util.ArrayList;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ScraperObject {
    String url;
    List<String> code;
    List<String> text;
    List<String> tags;

    public ScraperObject(Document doc) {
        this.url = doc.getString("url");
        this.code = doc.getList("code", String.class);
        this.text = doc.getList("text", String.class);
        this.tags = doc.getList("tags", String.class);
    }
}
