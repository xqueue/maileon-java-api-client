package com.maileon.api.mailings;

import java.io.Serializable;

public class Image implements Serializable {

    private String alt;

    private Integer height;

    private boolean hosted;

    private String src;

    private String title;

    private Integer width;

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public boolean isHosted() {
        return hosted;
    }

    public void setHosted(boolean hosted) {
        this.hosted = hosted;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    @Override
    public String toString() {
        return String
                .format("Image [alt=%s, height=%s, hosted=%s, src=%s, title=%s, width=%s]",
                        alt, height, hosted, src, title, width);
    }

}
