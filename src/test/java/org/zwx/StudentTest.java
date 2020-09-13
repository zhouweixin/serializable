package org.zwx;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import junit.framework.TestCase;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StudentTest extends TestCase {
    public void testSerializable() throws IOException {
        Student student1 = new Student("小明", 18, Sex.MALE, "王富贵", new Date(), new Teacher("李老师", 40));
        Student student2 = new Student("小花", 16, Sex.FEMALE, "钱很多", new Date(), new Teacher("赵老师", 38));
        List<Student> students = new ArrayList<>();
        students.add(student1);
        students.add(student2);

        ObjectMapper mapper = new ObjectMapper();
        String s = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(students);
        System.out.println(s);
    }

    public void testDeserializable() throws JsonProcessingException {
        String s = "{\"name\":\"小明\",\"age\":11,\"sex\":0,\"teacher_name\":\"李老师\",\"teacher_age\":40,\"father_name\":\"王富贵\",\"born_time\":\"2020-09-13 20:46:10\"}";
        ObjectMapper mapper = new ObjectMapper();
        Student student = mapper.readValue(s, Student.class);
        System.out.println(student);
    }

    public void testDeserializableStudents() throws JsonProcessingException {
        String s = "[{\"name\":\"小明\",\"age\":11,\"sex\":0,\"teacher_name\":\"李老师\",\"teacher_age\":40,\"father_name\":\"王富贵\",\"born_time\":\"2020-09-13 20:51:31\"},{\"name\":\"小花\",\"age\":9,\"sex\":1,\"teacher_name\":\"赵老师\",\"teacher_age\":38,\"father_name\":\"钱很多\",\"born_time\":\"2020-09-13 20:51:31\"}]";
        ObjectMapper mapper = new ObjectMapper();
        Student[] students = mapper.readValue(s, Student[].class);
        for (Student student : students) {
            System.out.println(student);
        }
    }

    public void testNonDefault() throws IOException {
        Student student = new Student("", 0, null, null, null, null);
        ObjectMapper mapper = new ObjectMapper();
        String s = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(student);
        System.out.println(s);
    }
}
