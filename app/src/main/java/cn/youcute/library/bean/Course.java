package cn.youcute.library.bean;

import java.io.Serializable;

/**
 * Created by jy on 2016/10/13.
 * 课程信息
 */

public class Course implements Serializable {
    public String name;
    public String teacher;
    public String lid;


    public Course(String name, String teacher, String lid) {
        this.name = name;
        this.teacher = teacher;
        this.lid = lid;
    }
}
