package org.anirudh.redquark.quarknews.model;

/**
 * POJO for one News Item
 */

public class NewsItem {

    private int id;
    private String title;
    private String url;
    private String publisher;
    private String category;
    private String hostname;
    private long timestamp;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean equals(Object o) {
        if (!(o instanceof NewsItem)) {
            return false;
        }
        NewsItem other = (NewsItem) o;
        return title.equals(other.title) && id == other.id;
    }

    public int hashCode() {
        return title.hashCode();
    }
}
