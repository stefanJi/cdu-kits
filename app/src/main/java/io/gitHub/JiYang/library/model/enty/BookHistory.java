package io.gitHub.JiYang.library.model.enty;

/**
 * Created by jy on 2016/9/22.
 * 历史借阅
 */
public class BookHistory {
    public String historyId;
    public String name;
    public String getData;
    public String endData;
    public String url;

    public BookHistory() {
    }

    public BookHistory(String historyId, String name, String getData, String endData) {
        this.historyId = historyId;
        this.name = name;
        this.getData = getData;
        this.endData = endData;
    }

    @Override
    public String toString() {
        return "BookHistoryFragment{" +
                "historyId='" + historyId + '\'' +
                ", name='" + name + '\'' +
                ", getData='" + getData + '\'' +
                ", endData='" + endData + '\'' +
                '}';
    }
}
