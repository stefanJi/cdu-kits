package io.gitHub.JiYang.library.bean;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BookFine bookFine = (BookFine) o;

        if (code != null ? !code.equals(bookFine.code) : bookFine.code != null) return false;
        if (getData != null ? !getData.equals(bookFine.getData) : bookFine.getData != null)
            return false;
        if (endData != null ? !endData.equals(bookFine.endData) : bookFine.endData != null)
            return false;
        if (shouldMoney != null ? !shouldMoney.equals(bookFine.shouldMoney) : bookFine.shouldMoney != null)
            return false;
        if (money != null ? !money.equals(bookFine.money) : bookFine.money != null) return false;
        return status != null ? status.equals(bookFine.status) : bookFine.status == null;

    }

    @Override
    public int hashCode() {
        int result = code != null ? code.hashCode() : 0;
        result = 31 * result + (getData != null ? getData.hashCode() : 0);
        result = 31 * result + (endData != null ? endData.hashCode() : 0);
        result = 31 * result + (shouldMoney != null ? shouldMoney.hashCode() : 0);
        result = 31 * result + (money != null ? money.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }
}
