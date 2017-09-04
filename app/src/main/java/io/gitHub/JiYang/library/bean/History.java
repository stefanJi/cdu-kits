package io.gitHub.JiYang.library.bean;

/**
 * Created by jy on 2016/9/22.
 * 历史借阅
 */
public class History {
    public String historyId;
    public String name;
    public String getData;
    public String endData;
    public String url;

    public History() {
    }

    public History(String historyId, String name, String getData, String endData) {
        this.historyId = historyId;
        this.name = name;
        this.getData = getData;
        this.endData = endData;
    }

    @Override
    public String toString() {
        return "History{" +
                "historyId='" + historyId + '\'' +
                ", name='" + name + '\'' +
                ", getData='" + getData + '\'' +
                ", endData='" + endData + '\'' +
                '}';
    }
}
