package com.inesa.hitnewalert.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data //lombok中的注解，自动生成getter&setter方法
@Component
//@TableName(value = "user")
public class User {
    private Integer id;
    private String name;
    private Integer age;
    private String email;
}

