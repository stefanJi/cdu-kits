package cn.youcute.library.bean;

import java.io.Serializable;

/**
 * Created by jy on 2016/10/13.
 * 课程信息
 */

public class Course implements Serializable {
    public String name;
    public String number;
    public String teacher;
    public String college;

    public Course(String name, String number, String teacher, String college) {
        this.name = name;
        this.number = number;
        this.teacher = teacher;
        this.college = college;
    }
}
