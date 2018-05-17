
package jiyang.cdu.kits.model.enty.zhihu;

import com.google.gson.annotations.Expose;

public class Editor {

    @Expose
    private String avatar;
    @Expose
    private String bio;
    @Expose
    private Long id;
    @Expose
    private String name;
    @Expose
    private String url;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
