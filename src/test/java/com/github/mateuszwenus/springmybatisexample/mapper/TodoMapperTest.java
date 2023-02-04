package com.github.mateuszwenus.springmybatisexample.mapper;

import com.github.mateuszwenus.springmybatisexample.domain.Todo;
import com.github.mateuszwenus.springmybatisexample.service.UpdateTodoCmd;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
public class TodoMapperTest {

    @Autowired
    TodoMapper todoMapper;
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Test
    public void shouldInsertNewTodo() {
        // given
        int count = JdbcTestUtils.countRowsInTable(jdbcTemplate, "TODOS");
        Todo todo = new Todo(UUID.randomUUID(), "title", "text", 0);
        // when
        todoMapper.insert(todo);
        // then
        assertThat(JdbcTestUtils.countRowsInTable(jdbcTemplate, "TODOS"), is(count + 1));
    }

    @Test
    public void shouldDetectNotExistingTodo() {
        // when
        boolean exists = todoMapper.exists(UUID.randomUUID());
        // then
        assertThat(exists, equalTo(false));
    }

    @Test
    public void shouldDetectExistingTodo() {
        // given
        Todo todo = new Todo(UUID.randomUUID(), "title", "text", 0);
        todoMapper.insert(todo);
        // when
        boolean exists = todoMapper.exists(todo.id());
        // then
        assertThat(exists, equalTo(true));
    }

    @Test
    public void shouldFindTodoById() {
        // given
        Todo todo = new Todo(UUID.randomUUID(), "title", "text", 0);
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
        Todo todo = new Todo(UUID.randomUUID(), "title", "text", 0);
        todoMapper.insert(todo);
        UpdateTodoCmd cmd = new UpdateTodoCmd(todo.id(), todo.title().toUpperCase(), todo.text().toUpperCase(), todo.version());
        // when
        int updateCount = todoMapper.update(cmd);
        // then
        assertThat(updateCount, is(1));
        Optional<Todo> optionalTodo = todoMapper.findById(cmd.id());
        assertThat(optionalTodo.isPresent(), is(true));
        assertThat(optionalTodo.get().title(), equalTo(cmd.title()));
        assertThat(optionalTodo.get().text(), equalTo(cmd.text()));
    }

    @Test
    public void shouldDeleteTodoById() {
        // given
        Todo todo = new Todo(UUID.randomUUID(), "title", "text", 0);
        todoMapper.insert(todo);
        int count = JdbcTestUtils.countRowsInTable(jdbcTemplate, "TODOS");
        // when
        int deleteCount = todoMapper.deleteById(todo.id());
        // then
        assertThat(deleteCount, equalTo(1));
        assertThat(JdbcTestUtils.countRowsInTable(jdbcTemplate, "TODOS"), is(count - 1));
    }

    @Test
    public void shouldDeleteAllTodos() {
        // given
        List.of(1, 2, 3).forEach(i -> todoMapper.insert(new Todo(UUID.randomUUID(), "title", "text", 0)));
        // when
        int deleteCount = todoMapper.deleteAll();
        // then
        assertThat(deleteCount, equalTo(3));
        assertThat(JdbcTestUtils.countRowsInTable(jdbcTemplate, "TODOS"), is(0));
    }
}
