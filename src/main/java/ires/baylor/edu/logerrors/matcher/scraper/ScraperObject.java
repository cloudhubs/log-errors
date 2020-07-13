package ires.baylor.edu.logerrors.matcher.scraper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.Document;

import java.util.List;
import java.util.Objects;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ScraperObject {
    String url;
    String title;
    List<String> code;
    List<String> text;
    List<String> tags;

    public ScraperObject(String title, List<String> text, List<String> code) {
        this.title = title;
        this.text = text;
        this.code = code;
    }
    public ScraperObject(List<String> text, List<String> code) {
        this.text = text;
        this.code = code;
    }
    public ScraperObject(String title, List<String> code) {
        this.title = title;
        this.code = code;
    }


    public ScraperObject(Document doc) {
        this.url = doc.getString("url");
        this.title = doc.getString("title");
        this.code = doc.getList("code", String.class);
        this.text = doc.getList("text", String.class);
        this.tags = doc.getList("tags", String.class);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ScraperObject)) return false;
        ScraperObject that = (ScraperObject) o;
        return Objects.equals(getUrl(), that.getUrl()) &&
                Objects.equals(getTitle(), that.getTitle()) &&
                Objects.equals(getCode(), that.getCode()) &&
                Objects.equals(getText(), that.getText()) &&
                Objects.equals(getTags(), that.getTags());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUrl(), getTitle(), getCode(), getText(), getTags());
    }
}
