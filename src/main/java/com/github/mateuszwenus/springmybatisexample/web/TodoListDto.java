package com.github.mateuszwenus.springmybatisexample.web;

import com.github.mateuszwenus.springmybatisexample.domain.Todo;

import java.util.UUID;

public record TodoListDto(UUID id, String title) {

    public TodoListDto(Todo input) {
        this(input.id(), input.title());
    }
}
