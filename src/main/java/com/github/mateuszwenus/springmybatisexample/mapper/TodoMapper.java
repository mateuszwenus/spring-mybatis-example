package com.github.mateuszwenus.springmybatisexample.mapper;

import com.github.mateuszwenus.springmybatisexample.domain.Todo;
import com.github.mateuszwenus.springmybatisexample.service.UpdateTodoCmd;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TodoMapper {
    boolean exists(UUID id);
    Optional<Todo> findById(UUID id);
    List<Todo> findAll();
    void insert(Todo todo);
    int update(UpdateTodoCmd todo);
    int deleteById(UUID id);
    int deleteAll();
}
