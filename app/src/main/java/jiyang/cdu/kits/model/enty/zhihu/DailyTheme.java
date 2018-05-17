
package jiyang.cdu.kits.model.enty.zhihu;

import com.google.gson.annotations.Expose;

import java.io.Serializable;


public class DailyTheme implements Serializable {

    @Expose
    private Long color;
    @Expose
    private String description;
    @Expose
    private Long id;
    @Expose
    private String name;
    @Expose
    private String thumbnail;

    public Long getColor() {
        return color;
    }

    public void setColor(Long color) {
        this.color = color;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    @Override
    public String toString() {
        return "DailyTheme{" +
                "description='" + description + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
