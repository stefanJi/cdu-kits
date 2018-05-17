
package jiyang.cdu.kits.model.enty.zhihu;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StoryContent {

    @Expose
    private Long id;

    @SerializedName("share_url")
    private String shareUrl;

    @Expose
    private String title;
    @Expose
    private Long type;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "StoryContent{" +
                ", id=" + id +
                ", shareUrl='" + shareUrl + '\'' +
                ", title='" + title + '\'' +
                ", type=" + type +
                '}';
    }
}
