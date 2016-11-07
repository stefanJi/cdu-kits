package cn.youcute.library.bean;

import java.io.Serializable;

/**
 * Created by jy on 2016/11/6.
 */

public class BannerBean implements Serializable {
    public String url;
    public String imageUrl;
    public String title;

    public BannerBean(String url, String imageUrl, String title) {
        this.url = url;
        this.imageUrl = imageUrl;
        this.title = title;
    }

}
