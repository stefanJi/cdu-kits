package io.gitHub.JiYang.library.model.enty;

import android.util.SparseArray;

import java.io.Serializable;


public class LibraryUserInfo implements Serializable {
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
     * 工作单位(学院)
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


    public LibraryUserInfo() {
    }

    public void setData(SparseArray<String> map) {
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
    }


    @Override
    public String toString() {
        return "LibraryUserInfo{" +
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
                '}';
    }
}
