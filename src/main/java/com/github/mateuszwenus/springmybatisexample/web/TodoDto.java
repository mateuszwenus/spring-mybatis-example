package com.github.mateuszwenus.springmybatisexample.web;

import com.github.mateuszwenus.springmybatisexample.domain.Todo;

import java.util.UUID;

public record TodoDto(UUID id, String title, String text) {

    public TodoDto(Todo input) {
        this(input.id(), input.title(), input.text());
    }
}
