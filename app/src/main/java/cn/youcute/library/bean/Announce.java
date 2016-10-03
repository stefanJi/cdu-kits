package cn.youcute.library.bean;

/**
 * Created by jy on 2016/9/25.
 * 通知公告
 */

public class Announce {
    public String title;
    public String data;
    public String url;

    public Announce(String title, String data, String url) {
        this.title = title;
        this.data = data;
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Announce announce = (Announce) o;

        return url != null ? url.equals(announce.url) : announce.url == null;

    }

    @Override
    public int hashCode() {
        return url != null ? url.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Announce{" +
                "title='" + title + '\'' +
                ", data='" + data + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
