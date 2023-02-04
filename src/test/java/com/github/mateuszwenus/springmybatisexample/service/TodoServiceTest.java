package com.github.mateuszwenus.springmybatisexample.service;

import com.github.mateuszwenus.springmybatisexample.domain.Todo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
public class TodoServiceTest {
    
    @Autowired
    TodoService todoService;
    
    @Test
    public void shouldReturnUpdatedTodo() {
        // given
        Todo todo = todoService.createTodo(new CreateTodoCmd("title", "text"));
        UpdateTodoCmd cmd = new UpdateTodoCmd(todo.id(), todo.title().toUpperCase(), todo.text().toUpperCase(), todo.version());
        // when
        Todo updatedTodo = todoService.updateTodo(cmd);
        // then
        assertThat(updatedTodo, notNullValue());
        assertThat(updatedTodo.id(), equalTo(cmd.id()));
        assertThat(updatedTodo.title(), equalTo(cmd.title()));
        assertThat(updatedTodo.text(), equalTo(cmd.text()));
        assertThat(updatedTodo.version(), equalTo(cmd.version() + 1));
    }

    @Test
    public void shouldThrowExceptionWhenVersionDoesntMatch() {
        // given
        Todo todo = todoService.createTodo(new CreateTodoCmd("title", "text"));
        UpdateTodoCmd cmd = new UpdateTodoCmd(todo.id(), todo.title().toUpperCase(), todo.text().toUpperCase(), -1);
        try {
            // when
            todoService.updateTodo(cmd);
            Assertions.fail("expected " + OptimisticLockingFailureException.class.getSimpleName());
        } catch (OptimisticLockingFailureException expected) {
            // then
        }
    }
}
