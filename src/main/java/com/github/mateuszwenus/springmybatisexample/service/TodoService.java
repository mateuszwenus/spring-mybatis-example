package com.github.mateuszwenus.springmybatisexample.service;

import com.github.mateuszwenus.springmybatisexample.domain.Todo;
import com.github.mateuszwenus.springmybatisexample.mapper.TodoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class TodoService {

    private final TodoMapper todoMapper;

    @Autowired
    public TodoService(TodoMapper todoMapper) {
        this.todoMapper = todoMapper;
    }

    @Transactional
    public Todo findById(UUID id) {
        return todoMapper
                .findById(id)
                .orElseThrow(() -> new TodoNotFoundException());
    }

    @Transactional
    public List<Todo> findAll() {
        return todoMapper.findAll();
    }

    @Transactional
    public Todo createTodo(CreateTodoCmd cmd) {
        Todo todo = new Todo(UUID.randomUUID(), cmd.title(), cmd.text());
        todoMapper.insert(todo);
        return todo;
    }

    @Transactional
    public Todo updateTodo(UpdateTodoCmd cmd) {
        if (!todoMapper.exists(cmd.id())) {
            throw new TodoNotFoundException();
        }
        int updateCount = todoMapper.update(cmd);
        if (updateCount == 0) {
            throw new OptimisticLockingFailureException("Todo " + cmd.id() + " modified after it was read");
        }
        return findById(cmd.id());
    }

    @Transactional
    public void deleteTodo(DeleteTodoCmd cmd) {
        todoMapper.deleteById(cmd.id());
    }

    @Transactional
    public void deleteAll() {
        todoMapper.deleteAll();
    }
}
