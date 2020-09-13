package org.zwx;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Date;

@JsonInclude(value = JsonInclude.Include.NON_DEFAULT)
public class Student {
    private String name;

    @JsonSerialize(using = AgeSerializer.class)
    @JsonDeserialize(using = AgeDeserializer.class)
    private Integer age;
    private Sex sex;

    @JsonProperty("father_name")
    private String fatherName;

    @JsonProperty("born_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date bornTime;

    @JsonUnwrapped
    private Teacher teacher;

    public Student() {
    }

    public Student(String name, Integer age, Sex sex, String fatherName, Date bornTime, Teacher teacher) {
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.fatherName = fatherName;
        this.bornTime = bornTime;
        this.teacher = teacher;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public Date getBornTime() {
        return bornTime;
    }

    public void setBornTime(Date bornTime) {
        this.bornTime = bornTime;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", sex=" + sex +
                ", fatherName='" + fatherName + '\'' +
                ", bornTime=" + bornTime +
                ", teacher=" + teacher +
                '}';
    }
}
