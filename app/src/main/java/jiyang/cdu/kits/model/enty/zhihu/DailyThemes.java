
package jiyang.cdu.kits.model.enty.zhihu;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class DailyThemes {

    @SerializedName("limit")
    private Long limit;
    @SerializedName("others")
    private List<DailyTheme> dailyThemes;
    @SerializedName("subscribed")
    private List<Object> subscribed;

    public Long getLimit() {
        return limit;
    }

    public void setLimit(Long limit) {
        this.limit = limit;
    }

    public List<DailyTheme> getDailyThemes() {
        return dailyThemes;
    }

    public void setDailyThemes(List<DailyTheme> dailyThemes) {
        this.dailyThemes = dailyThemes;
    }

    public List<Object> getSubscribed() {
        return subscribed;
    }

    public void setSubscribed(List<Object> subscribed) {
        this.subscribed = subscribed;
    }

    @Override
    public String toString() {
        return "DailyThemes{" +
                "limit=" + limit +
                ", dailyThemes=" + dailyThemes +
                ", subscribed=" + subscribed +
                '}';
    }
}
