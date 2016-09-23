package cn.youcute.library.bean;

/**
 * Created by jy on 2016/9/23.
 * 有违章欠款的书
 */
public class BookFine {
    public String code;//编号
    public String name;//书名
    public String getData;//借阅日
    public String endData;//应还日
    public String shouldMoney;//应缴
    public String money;//实缴
    public String status;//状态

    public BookFine() {
    }

    public BookFine(String code, String name, String getData, String endData, String shouldMoney, String money, String status) {
        this.code = code;
        this.name = name;
        this.getData = getData;
        this.endData = endData;
        this.shouldMoney = shouldMoney;
        this.money = money;
        this.status = status;
    }

}
