
package jiyang.cdu.kits.model.enty;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Release {

    @Expose
    public String body;
    @SerializedName("created_at")
    public String createdAt;
    @SerializedName("html_url")
    public String htmlUrl;
    @Expose
    public String name;
    @SerializedName("published_at")
    public String publishedAt;
    @SerializedName("tag_name")
    public String tagName;
}
