package io.gitHub.JiYang.library.model.enty;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

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
    public String check;    //续借编号
    public String url;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Book book = (Book) o;

        return code != null ? code.equals(book.code) : book.code == null && (author != null ? author.equals(book.author) : book.author == null && name.equals(book.name));

    }

    public int getOutOfDateDays() {
        int days = 0;
        Calendar cal1 = new GregorianCalendar();
        Calendar cal2 = new GregorianCalendar();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        try {
            cal1.setTime(new Date());
            cal2.setTime(sdf.parse(endData));
            days = (int) ((cal2.getTime().getTime() - cal1.getTime().getTime()) / (1000 * 60 * 60 * 24));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return days;
    }

    @Override
    public int hashCode() {
        int result = code != null ? code.hashCode() : 0;
        result = 31 * result + (author != null ? author.hashCode() : 0);
        result = 31 * result + name.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Book{" +
                "code='" + code + '\'' +
                ", author='" + author + '\'' +
                ", name='" + name + '\'' +
                ", getData='" + getData + '\'' +
                ", endData='" + endData + '\'' +
                ", getCount='" + getCount + '\'' +
                ", count='" + count + '\'' +
                ", check='" + check + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
