package cn.youcute.library.bean;

/**
 * Created by jy on 2016/9/23.
 * 借书
 */
public class Book {
    public String code;//条码号
    public String name;//书名
    public String author;//作者
    public String getData;//借阅日期
    public String endData;//应还日期
    public String getCount;//续借量

    public Book() {
    }

    public Book(String code, String name, String author, String getData, String endData, String getCount) {
        this.code = code;
        this.name = name;
        this.author = author;
        this.getData = getData;
        this.endData = endData;
        this.getCount = getCount;
    }
}
