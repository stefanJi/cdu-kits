
package jiyang.cdu.kits.model.enty.zhihu;

import com.google.gson.annotations.Expose;

import java.util.List;

public class Story {

    @Expose
    private Long id;
    @Expose
    private List<String> images;
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

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
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
        return "Story{" +
                "id=" + id +
                ", images=" + images +
                ", title='" + title + '\'' +
                ", type=" + type +
                '}';
    }
}
