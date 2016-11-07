package cn.youcute.library.bean;

import java.io.Serializable;

/**
 * Created by jy on 2016/11/6.
 */

public class Album implements Serializable {
    public String url;
    public String author;

    public Album(String url, String author) {
        this.url = url;
        this.author = author;
    }
}
