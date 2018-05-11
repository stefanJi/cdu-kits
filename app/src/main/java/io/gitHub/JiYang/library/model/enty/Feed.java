package io.gitHub.JiYang.library.model.enty;

public class Feed {
    public String title;
    public String date;
    public String url;

    public Feed(String title, String date, String url) {
        this.title = title;
        this.date = date;
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Feed feed = (Feed) o;
        return url != null ? url.equals(feed.url) : feed.url == null;
    }

    @Override
    public int hashCode() {
        return url != null ? url.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Feed{" +
                "title='" + title + '\'' +
                ", date='" + date + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
