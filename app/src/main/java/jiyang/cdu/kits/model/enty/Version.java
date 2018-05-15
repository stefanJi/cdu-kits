package jiyang.cdu.kits.model.enty;

import com.google.gson.annotations.SerializedName;

public class Version {
    @SerializedName("versionCode")
    public int versionCode;

    @SerializedName("versionName")
    public String versionName;

    @SerializedName("url")
    public String url;

    @SerializedName("description")
    public String description;

    @Override
    public String toString() {
        return "Version{" +
                "versionCode=" + versionCode +
                ", versionName='" + versionName + '\'' +
                ", url='" + url + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
