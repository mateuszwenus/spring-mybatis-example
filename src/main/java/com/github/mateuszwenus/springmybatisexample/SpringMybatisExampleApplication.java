package com.github.mateuszwenus.springmybatisexample;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.github.mateuszwenus.springmybatisexample.mapper")
public class SpringMybatisExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringMybatisExampleApplication.class, args);
	}

}
