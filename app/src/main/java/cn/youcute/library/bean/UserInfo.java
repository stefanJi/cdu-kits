package cn.youcute.library.bean;

import java.util.Map;

/**
 * Created by jy on 2016/9/21.
 * 用户信息
 */
public class UserInfo {
    /**
     * 姓名、学号、条码号
     */
    public String name, account, number;
    /**
     * 失效日期、办证日期、生效日期
     */
    public String newData, outData, okData;
    /**
     * 最大可借图书、最大可预约图书、最大可委托图书
     */
    public String maxBooks, maxPlanBooks, maxWeituoBooks;
    /**
     * 读者类型，借阅等级，累计借书
     */
    public String type, index, bookCount;
    /**
     * 违章次数,欠款金额
     */
    public String wrongCount, wrongMoney;
    /**
     * 系别
     */
    public String belong;
    /**
     * 邮箱
     */
    public String email;
    /**
     * 身份证号
     */
    public String idCardNumber;
    /**
     * 工作单位
     */
    public String belongClass;
    /**
     * 专业/职位
     */
    public String learnType;
    /**
     *
     */
    public String studentClass;
    /**
     * 性别
     */
    public String sex;
    /**
     * 住址
     */
    public String address;
    /**
     * 电话
     */
    public String callNumber;
    /**
     * 手机
     */
    public String phoneNumber;
    /**
     * 出生日期
     */
    public String bodyDate;
    /**
     * 文化程度
     */
    public String learnIndex;
    /**
     * 押金
     */
    public String totalMoney;
    /**
     * 手续费
     */
    public String useMoney;

    public UserInfo() {
    }

    public UserInfo(String name,
                    String account,
                    String number,
                    String newData,
                    String outData,
                    String okData,
                    String maxBooks,
                    String maxPlanBooks,
                    String maxWeituoBooks,
                    String type,
                    String index,
                    String bookCount,
                    String wrongCount,
                    String wrongMoney,
                    String belong,
                    String email,
                    String idCardNumber,
                    String belongClass,
                    String learnType,
                    String studentClass,
                    String sex,
                    String address,
                    String callNumber,
                    String phoneNumber,
                    String bodyDate,
                    String learnIndex,
                    String totalMoney,
                    String useMoney) {
        this.name = name;
        this.account = account;
        this.number = number;
        this.newData = newData;
        this.outData = outData;
        this.okData = okData;
        this.maxBooks = maxBooks;
        this.maxPlanBooks = maxPlanBooks;
        this.maxWeituoBooks = maxWeituoBooks;
        this.type = type;
        this.index = index;
        this.bookCount = bookCount;
        this.wrongCount = wrongCount;
        this.wrongMoney = wrongMoney;
        this.belong = belong;
        this.email = email;
        this.idCardNumber = idCardNumber;
        this.belongClass = belongClass;
        this.learnType = learnType;
        this.studentClass = studentClass;
        this.sex = sex;
        this.address = address;
        this.callNumber = callNumber;
        this.phoneNumber = phoneNumber;
        this.bodyDate = bodyDate;
        this.learnIndex = learnIndex;
        this.totalMoney = totalMoney;
        this.useMoney = useMoney;
    }

    public UserInfo(Map<Integer, String> map) {
        name = map.get(1);
        account = map.get(2);
        number = map.get(3);
        newData = map.get(4);
        outData = map.get(5);
        okData = map.get(6);
        maxBooks = map.get(7);
        maxPlanBooks = map.get(8);
        maxWeituoBooks = map.get(9);
        type = map.get(10);
        index = map.get(11);
        bookCount = map.get(12);
        wrongCount = map.get(13);
        wrongMoney = map.get(14);
        belong = map.get(15);
        email = map.get(16);
        idCardNumber = map.get(17);
        belongClass = map.get(18);
        learnType = map.get(19);
        studentClass = map.get(20);
        sex = map.get(21);
        address = map.get(22);
        //map.get(23);邮编
        callNumber = map.get(24);
        phoneNumber = map.get(25);
        bodyDate = map.get(26);
        learnIndex = map.get(27);
        totalMoney = map.get(28);
        useMoney = map.get(29);
    }

    public UserInfo(String name, String account, String learnType) {
        this.name = name;
        this.account = account;
        this.learnType = learnType;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "name='" + name + '\'' +
                ", account='" + account + '\'' +
                ", number='" + number + '\'' +
                ", newData='" + newData + '\'' +
                ", outData='" + outData + '\'' +
                ", okData='" + okData + '\'' +
                ", maxBooks='" + maxBooks + '\'' +
                ", maxPlanBooks='" + maxPlanBooks + '\'' +
                ", maxWeituoBooks='" + maxWeituoBooks + '\'' +
                ", type='" + type + '\'' +
                ", index='" + index + '\'' +
                ", bookCount='" + bookCount + '\'' +
                ", wrongCount='" + wrongCount + '\'' +
                ", wrongMoney='" + wrongMoney + '\'' +
                ", belong='" + belong + '\'' +
                ", email='" + email + '\'' +
                ", idCardNumber='" + idCardNumber + '\'' +
                ", belongClass='" + belongClass + '\'' +
                ", learnType='" + learnType + '\'' +
                ", studentClass='" + studentClass + '\'' +
                ", sex='" + sex + '\'' +
                ", address='" + address + '\'' +
                ", callNumber='" + callNumber + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", bodyDate='" + bodyDate + '\'' +
                ", learnIndex='" + learnIndex + '\'' +
                ", totalMoney='" + totalMoney + '\'' +
                ", useMoney='" + useMoney + '\'' +
                '}';
    }
}
