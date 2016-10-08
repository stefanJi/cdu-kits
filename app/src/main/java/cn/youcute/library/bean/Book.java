package cn.youcute.library.bean;

/**
 * Created by jy on 2016/9/23.
 * 借书
 */
public class Book {
    public String code;//条码号
    public String author;
    public String name;//书名
    public String getData;//借阅日期
    public String endData;//应还日期
    public String getCount;//续借量
    public String count;    //可借数量

    public Book() {
    }

    //已借书籍
    public Book(String code, String name, String getData, String endData, String getCount) {
        this.code = code;
        this.name = name;
        this.getData = getData;
        this.endData = endData;
        this.getCount = getCount;
    }

    //可借书籍
    public Book(String code, String name, String count) {
        this.code = code;
        this.name = name;
        this.count = count;
    }

    @Override
    public String toString() {
        return "Book{" +
                "count='" + count + '\'' +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
