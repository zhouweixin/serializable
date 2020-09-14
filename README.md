# jackson序列化与反序列化的应用实践

源码地址:
https://github.com/zhouweixin/serializable

## 1 相关概念

1. 序列化: 把对象转换为字节序列的过程称为对象的序列化
2. 反序列化: 把字节序列恢复为对象的过程称为对象的反序列化

## 2 序列化的作用

1. 用于把内存中的对象状态保存到一个文件中或者数据库中
2. 用于网络传送对象
3. 用于远程调用传输对象

## 3 准备序列化对象

准备了两个类, 教师类和学生类, 其中一个学生只有一个教师
这里省略了构造方法和setter, getter方法

Teacher.java
```java
public class Teacher {
    private String name;
    private Integer age;
}
```

Student.java
```java
package org.zwx;

public class Student {
    private String name;
    private Integer age;
    private Sex sex;
    private String fatherName;
    private Date bornTime;
    private Teacher teacher;
}
```

Sex.java
```java
public enum Sex {
    MALE("男"), FEMALE("女");

    private String name;
    
    Sex(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
```

## 4 引入jackson依赖
本示例是基于gradle的, 从maven中心仓库中选择了2.11.2版本的jackson-databind
```
compile group: 'com.fasterxml.jackson.core', name: 'jackson-databind', version: '2.11.2'
```

## 5 序列化与格式化输出

### 5.1 流程
1. 首先需要有一个待序列化对象, 本例中的student对象
2. 创建一个对象映射器, jackson包下的ObjectMapper
3. 调用序列化函数, 本例中的writeValueAsString, 将对象转为字符串, 便于展示

### 5.2 代码
```java
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
```

### 5.3 结果
```shell script
[ {
  "name" : "小明",
  "age" : 18,
  "sex" : "MALE",
  "fatherName" : "王富贵",
  "bornTime" : 1599996926917,
  "teacher" : {
    "name" : "李老师",
    "age" : 40
  }
}, {
  "name" : "小花",
  "age" : 16,
  "sex" : "FEMALE",
  "fatherName" : "钱很多",
  "bornTime" : 1599996926917,
  "teacher" : {
    "name" : "赵老师",
    "age" : 38
  }
} ]
```

### 5.4 分析

1. 示例中调用了方法writerWithDefaultPrettyPrinter, 美化了json的格式
2. 否则将打印`[{"name":"小明","age":18,"sex":"MALE","fatherName":"王富贵","bornTime":1599997061097,"teacher":{"name":"李老师","age":40}},{"name":"小花","age":16,"sex":"FEMALE","fatherName":"钱很多","bornTime":1599997061097,"teacher":{"name":"赵老师","age":38}}]`

## 6 自定义序列化的名字

### 6.1 场景
假如需要将序列化的json由驼峰命名修改为下划线命名, 如fatherName修改为father_name

只需要在字段fatherName上用注解JsonProperty配置

### 6.2 示例代码
```java
@JsonProperty("father_name")
private String fatherName;
@JsonProperty("born_time")
private Date bornTime;
```

### 6.3 示例结果
```shell script
[ {
  "name" : "小明",
  "age" : 18,
  "sex" : "MALE",
  "teacher" : {
    "name" : "李老师",
    "age" : 40
  },
  "father_name" : "王富贵",
  "born_time" : 1599997157609
}, {
  "name" : "小花",
  "age" : 16,
  "sex" : "FEMALE",
  "teacher" : {
    "name" : "赵老师",
    "age" : 38
  },
  "father_name" : "钱很多",
  "born_time" : 1599997157610
} ]
```

## 7 自定义输出格式

### 7.1 bornTime格式设置
当前bornTime的格式为unix时间戮, 可读性非常差

现修改为`yyyy-MM-dd HH:mm:ss`
并设置时区为东八区

示例代码
```java
@JsonProperty("born_time")
@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
private Date bornTime;
```

结果
```shell script
[ {
  "name" : "小明",
  "age" : 18,
  "sex" : "MALE",
  "teacher" : {
    "name" : "李老师",
    "age" : 40
  },
  "father_name" : "王富贵",
  "born_time" : "2020-09-13 19:50:47"
}, {
  "name" : "小花",
  "age" : 16,
  "sex" : "FEMALE",
  "teacher" : {
    "name" : "赵老师",
    "age" : 38
  },
  "father_name" : "钱很多",
  "born_time" : "2020-09-13 19:50:47"
} ]
```

### 7.2 sex设置为中文
只需要为Sex添加一个方法getOrdinal, 并添加注解JsonValue即可

示例代码
```java
@JsonValue
public String getOrdinal() {
    return name;
}
```

示例结果
```shell script
[ {
  "name" : "小明",
  "age" : 18,
  "sex" : "男",
  "teacher" : {
    "name" : "李老师",
    "age" : 40
  },
  "father_name" : "王富贵",
  "born_time" : "2020-09-13 19:57:47"
}, {
  "name" : "小花",
  "age" : 16,
  "sex" : "女",
  "teacher" : {
    "name" : "赵老师",
    "age" : 38
  },
  "father_name" : "钱很多",
  "born_time" : "2020-09-13 19:57:47"
} ]
```

### 7.3 sex设置为序号
有些场景喜欢用0和1等序号设置男女, 即枚举的序号: 0表示男, 1表示女

此时需要修改Set的getOrdinal方法
1. 修改返回值类型为int
2. 调用父类的getOrdinal方法

示例代码
```java
@JsonValue
public int getOrdinal() {
    return super.ordinal();
}
```

示例结果
```shell script
[ {
  "name" : "小明",
  "age" : 18,
  "sex" : 0,
  "teacher" : {
    "name" : "李老师",
    "age" : 40
  },
  "father_name" : "王富贵",
  "born_time" : "2020-09-13 20:01:44"
}, {
  "name" : "小花",
  "age" : 16,
  "sex" : 1,
  "teacher" : {
    "name" : "赵老师",
    "age" : 38
  },
  "father_name" : "钱很多",
  "born_time" : "2020-09-13 20:01:44"
} ]
```

## 8 拍平嵌套类型

### 场景
如前面提到的结果所示, teacher的两个属性并不在student的第一层, 
有时可能会更深的层次, 使用起来不太友好

如何用teacher_name和teacher_age两个属性代替teacher呢?

1. 在Student的teacher属性上添加注解JsonUnwrapped, 意为不包裹
2. 在Teacher的属性上利用注解JsonProperty重命名

### 示例代码
Student.java
```java
@JsonUnwrapped
private Teacher teacher;
```

Teacher.java
```java
@JsonProperty("teacher_name")
private String name;
@JsonProperty("teacher_age")
private Integer age;
```

### 示例结果
```shell script
[ {
  "name" : "小明",
  "age" : 18,
  "sex" : 0,
  "teacher_name" : "李老师",
  "teacher_age" : 40,
  "father_name" : "王富贵",
  "born_time" : "2020-09-13 20:21:53"
}, {
  "name" : "小花",
  "age" : 16,
  "sex" : 1,
  "teacher_name" : "赵老师",
  "teacher_age" : 38,
  "father_name" : "钱很多",
  "born_time" : "2020-09-13 20:21:53"
} ]
```

## 9 自定义序列化器

### 9.1 场景
假如需要将年龄调整为理论学龄, 即将年龄减去7, 得到理论学龄, 如何操作呢?

1. 创建自定义年龄序列化器AgeSerializer, 继承StdSerializer<>
    1. 创建AgeSerializer的构造方法
    2. 重写serialize函数
2. 利用注解修指定Student属性age的序列化器AgeSerializer

### 9.2 示例代码
AgeSerializer.java
```java
public class AgeSerializer extends StdSerializer<Integer> {
    protected AgeSerializer() {
        super(Integer.class);
    }

    @Override
    public void serialize(Integer value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeNumber(value - 7);
    }
}
```

Student.java
```java
@JsonSerialize(using = AgeSerializer.class)
private Integer age;
```

### 9.3 示例结果
```shell script
[ {
  "name" : "小明",
  "age" : 11,
  "sex" : 0,
  "teacher_name" : "李老师",
  "teacher_age" : 40,
  "father_name" : "王富贵",
  "born_time" : "2020-09-13 20:31:59"
}, {
  "name" : "小花",
  "age" : 9,
  "sex" : 1,
  "teacher_name" : "赵老师",
  "teacher_age" : 38,
  "father_name" : "钱很多",
  "born_time" : "2020-09-13 20:31:59"
} ]
```

## 10 反序列化

### 10.1 流程

1. 首先需要有序列化好的数据, 可以是string, byte[], 文件二进制等
2. 创建一个对象映射器, jackson包下的ObjectMapper
3. 调用反序列化函数, 本例中的readValue, 将字符串转为对象

### 10.2 反序列化对象数据

示例代码
```java
public void testDeserializable() throws JsonProcessingException {
    String s = "{\"name\":\"小明\",\"age\":11,\"sex\":0,\"teacher_name\":\"李老师\",\"teacher_age\":40,\"father_name\":\"王富贵\",\"born_time\":\"2020-09-13 20:46:10\"}";
    ObjectMapper mapper = new ObjectMapper();
    Student student = mapper.readValue(s, Student.class);
    System.out.println(student);
}
```

示例结果
```shell script
Student{name='小明', age=11, sex=MALE, fatherName='王富贵', bornTime=Sun Sep 13 20:46:10 CST 2020, teacher=Teacher{name='李老师', age=40}}
```

分析
1. 为了便于打印对象数据, 重写了Student和Teacher的toString方法
2. 从数据中可以看出, age的结果是错误的, 原因在于之前自定义的序列化器将年龄减小了7, 10.4节将会通过自定义反序列化器来解决此问题

### 10.3 反序列化对象数组数据

示例代码
```java
public void testDeserializableStudents() throws JsonProcessingException {
    String s = "[{\"name\":\"小明\",\"age\":11,\"sex\":0,\"teacher_name\":\"李老师\",\"teacher_age\":40,\"father_name\":\"王富贵\",\"born_time\":\"2020-09-13 20:51:31\"},{\"name\":\"小花\",\"age\":9,\"sex\":1,\"teacher_name\":\"赵老师\",\"teacher_age\":38,\"father_name\":\"钱很多\",\"born_time\":\"2020-09-13 20:51:31\"}]";
    ObjectMapper mapper = new ObjectMapper();
    Student[] students = mapper.readValue(s, Student[].class);
    for (Student student : students) {
        System.out.println(student);
    }
}
```

示例结果

```shell script
Student{name='小明', age=11, sex=MALE, fatherName='王富贵', bornTime=Sun Sep 13 20:51:31 CST 2020, teacher=Teacher{name='李老师', age=40}}
Student{name='小花', age=9, sex=FEMALE, fatherName='钱很多', bornTime=Sun Sep 13 20:51:31 CST 2020, teacher=Teacher{name='赵老师', age=38}}
```

分析
1. readValue的第二个参数需要传类型, 这里推荐用数组, 不推荐用List, 具体原因笔者目前也没花时间去研究

### 10.4 自定义反序列化器

从10.2节及10.3的现象中可以看出来, 仅仅自定义的序列化器会导致序列化的过程是正常的, 反序列化的过程仍然是默认逻辑, 有时候会导致意想不到的结果

遇到此场景, 可以考虑自定义反序列化器

1. 创建自定义反序列化器AgeDeserializer, 继承StdDeserializer<>
2. 重写deserialize方法
3. 在Student的age属性上添加注解JsonDeserialize, 并指定反序列化器AgeDeserializer

示例代码

AgeDeserializer.java
```java
public class AgeDeserializer extends JsonDeserializer<Integer> {
    @Override
    public Integer deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        return p.getIntValue() + 7;
    }
}
```

Student.java
```java
@JsonSerialize(using = AgeSerializer.class)
@JsonDeserialize(using = AgeDeserializer.class)
private Integer age;
```

示例结果
```shell script
Student{name='小明', age=18, sex=MALE, fatherName='王富贵', bornTime=Sun Sep 13 20:51:31 CST 2020, teacher=Teacher{name='李老师', age=40}}
Student{name='小花', age=16, sex=FEMALE, fatherName='钱很多', bornTime=Sun Sep 13 20:51:31 CST 2020, teacher=Teacher{name='赵老师', age=38}}
```

## 11 注解JsonInclude

该注解使用在实体类上, 格式`@JsonInclude(value = JsonInclude.Include.NON_DEFAULT)`

其中, Include有7种参数, 功能对比如下

|参数|功能|备注|
|----|----|----|
|Include.ALWAYS|属性总是序列化(需要有get方法)|默认值|
|Include.NON_DEFAULT|属性为默认值不序列化|如: int:0, bool:false|
|Include.NON_EMPTY|属性为空("")或null不序列化||
|Include.NON_NULL|属性为null不序列化||
|Include.CUSTOM|||
|Include.USE_DEFAULTS|||
|Include.NON_ABSENT|||

### 代码示例
Student.java
```java
@JsonInclude(value = JsonInclude.Include.NON_DEFAULT)
public class Student {
```

```java
public void testNonDefault() throws IOException {
    Student student = new Student("", 0, null, null, null, null);
    ObjectMapper mapper = new ObjectMapper();
    String s = mapper.writeValueAsString(student);
    System.out.println(s);
}
```

### 示例输出
```shell script
{
  "name" : "",
  "age" : -7
}
```

### 分析
1. 当属性为默认值, 即零值时, 不序列化
2. 常见的零值:
    1. int: 0
    2. bool: false,
    3. String: null

## 12 注解JsonIgnoreProperties

该注解为类注解, 配置忽略序列化和反序列化的字段名
如下所示, 忽略字段name和age

```java
@JsonIgnoreProperties(value = {"name", "age"})
@JsonInclude(value = JsonInclude.Include.NON_DEFAULT)
public class Student {
```

## 13 注解JsonIgnore

该注解为属性注解, 表示忽略当前属性, 如下所示, 表示忽略name字段

```java
@JsonIgnore
private String name;
```


