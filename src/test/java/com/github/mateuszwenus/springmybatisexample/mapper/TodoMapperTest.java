package com.github.mateuszwenus.springmybatisexample.mapper;

import com.github.mateuszwenus.springmybatisexample.domain.Todo;
import com.github.mateuszwenus.springmybatisexample.service.UpdateTodoCmd;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class TodoMapperTest {

    @Autowired
    TodoMapper todoMapper;
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Test
    public void shouldInsertNewTodo() {
        // given
        int count = JdbcTestUtils.countRowsInTable(jdbcTemplate, "TODOS");
        Todo todo = new Todo(UUID.randomUUID(), "title", "text");
        // when
        todoMapper.insert(todo);
        // then
        assertThat(JdbcTestUtils.countRowsInTable(jdbcTemplate, "TODOS"), is(count + 1));
    }

    @Test
    public void shouldFindTodoById() {
        // given
        Todo todo = new Todo(UUID.randomUUID(), "title", "text");
        todoMapper.insert(todo);
        // when
        Optional<Todo> optionalTodo = todoMapper.findById(todo.id());
        // then
        assertThat(optionalTodo.isPresent(), is(true));
        assertThat(optionalTodo.get(), equalTo(todo));
    }

    @Test
    public void shouldReturnEmptyOptionalWhenTodoNotExists() {
        // when
        Optional<Todo> optionalTodo = todoMapper.findById(UUID.randomUUID());
        // then
        assertThat(optionalTodo.isEmpty(), is(true));
    }

    @Test
    public void shouldUpdateTodo() {
        // given
        Todo todo = new Todo(UUID.randomUUID(), "title", "text");
        todoMapper.insert(todo);
        UpdateTodoCmd cmd = new UpdateTodoCmd(todo.id(), todo.title().toUpperCase(), todo.text().toUpperCase());
        // when
        todoMapper.update(cmd);
        // then
        Optional<Todo> optionalTodo = todoMapper.findById(cmd.id());
        assertThat(optionalTodo.isPresent(), is(true));
        assertThat(optionalTodo.get().title(), equalTo(cmd.title()));
        assertThat(optionalTodo.get().text(), equalTo(cmd.text()));
    }

    @Test
    public void shouldDeleteTodoById() {
        // given
        Todo todo = new Todo(UUID.randomUUID(), "title", "text");
        todoMapper.insert(todo);
        int count = JdbcTestUtils.countRowsInTable(jdbcTemplate, "TODOS");
        // when
        todoMapper.deleteById(todo.id());
        // then
        assertThat(JdbcTestUtils.countRowsInTable(jdbcTemplate, "TODOS"), is(count - 1));
    }

    @Test
    public void shouldDeleteAllTodos() {
        // given
        List.of(1, 2, 3).forEach(i -> todoMapper.insert(new Todo(UUID.randomUUID(), "title", "text")));
        // when
        todoMapper.deleteAll();
        // then
        assertThat(JdbcTestUtils.countRowsInTable(jdbcTemplate, "TODOS"), is(0));
    }
}
