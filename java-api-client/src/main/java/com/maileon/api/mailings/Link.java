package com.maileon.api.mailings;

import java.io.Serializable;
import java.util.List;

public class Link implements Serializable {

    private String format;

    private Long id;

    private String layout;

    private List<String> tags;

    private String url;

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return String.format(
                "Link [format=%s, id=%s, layout=%s, tags=%s, url=%s]", format,
                id, layout, tags, url);
    }

}
