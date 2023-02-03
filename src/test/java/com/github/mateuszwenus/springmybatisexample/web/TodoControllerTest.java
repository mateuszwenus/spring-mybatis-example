package com.github.mateuszwenus.springmybatisexample.web;

import com.github.mateuszwenus.springmybatisexample.domain.Todo;
import com.github.mateuszwenus.springmybatisexample.service.CreataTodoCmd;
import com.github.mateuszwenus.springmybatisexample.service.TodoService;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TodoControllerTest {

    @LocalServerPort
    int port;

    @Autowired
    TodoService todoService;

    @BeforeEach
    public void beforeTest() {
        todoService.deleteAll();
    }

    @Test
    public void shouldReturnEmptyList() {
        given()
                .accept(ContentType.JSON)
                .log()
                .all(true)
        .when()
                .get("http://localhost:" + port + "/todos")
        .then()
                .log()
                .all(true)
                .and()
                .statusCode(is(200))
                .contentType(ContentType.JSON)
                .body(is("[]"));
    }

    @Test
    public void shouldReturnHttp404WhenTodoNotFound() {
        given()
                .accept(ContentType.JSON)
                .log()
                .all(true)
        .when()
                .get("http://localhost:" + port + "/todos/" + UUID.randomUUID())
        .then()
                .log()
                .all(true)
                .and()
                .statusCode(is(404));
    }

    @Test
    public void shouldCreateNewTodoItem() {
        CreateTodoRequest req = new CreateTodoRequest("title", "text");
        given()
                .contentType(ContentType.JSON)
                .body(req)
                .accept(ContentType.JSON)
                .log()
                .all(true)
        .when()
                .post("http://localhost:" + port + "/todos")
        .then()
                .log()
                .all(true)
                .and()
                .statusCode(is(200))
                .contentType(ContentType.JSON)
                .body("id", notNullValue())
                .body("title", is(req.getTitle()))
                .body("text", is(req.getText()));
    }

    @Test
    public void shouldReturnListWithCreatedTodoItem() {
        Todo todo = todoService.createTodo(new CreataTodoCmd("title", "text"));
        given()
                .accept(ContentType.JSON)
                .log()
                .all(true)
        .when()
                .get("http://localhost:" + port + "/todos")
        .then()
                .log()
                .all(true)
                .and()
                .statusCode(is(200))
                .contentType(ContentType.JSON)
                .body("[0].id", is(todo.id().toString()))
                .body("[0].title", is(todo.title()));
    }

    @Test
    public void shouldReturnCreatedTodoItem() {
        Todo todo = todoService.createTodo(new CreataTodoCmd("title", "text"));
        given()
                .accept(ContentType.JSON)
                .log()
                .all(true)
        .when()
                .get("http://localhost:" + port + "/todos/" + todo.id())
        .then()
                .log()
                .all(true)
                .and()
                .statusCode(is(200))
                .contentType(ContentType.JSON)
                .body("id", is(todo.id().toString()))
                .body("title", is(todo.title()))
                .body("text", is(todo.text()));
    }

    @Test
    public void shouldUpdateTodoItem() {
        Todo todo = todoService.createTodo(new CreataTodoCmd("title", "text"));
        UpdateTodoRequest req = new UpdateTodoRequest("title2", "text2");
        given()
                .contentType(ContentType.JSON)
                .body(req)
                .accept(ContentType.JSON)
                .log()
                .all(true)
        .when()
                .put("http://localhost:" + port + "/todos/" + todo.id())
        .then()
                .log()
                .all(true)
                .and()
                .statusCode(is(200))
                .contentType(ContentType.JSON)
                .body("id", is(todo.id().toString()))
                .body("title", is(req.getTitle()))
                .body("text", is(req.getText()));
    }

    @Test
    public void shouldReturnHttp404WhenUpdatedTodoNotFound() {
        UUID id = UUID.randomUUID();
        UpdateTodoRequest req = new UpdateTodoRequest("title2", "text2");
        given()
                .contentType(ContentType.JSON)
                .body(req)
                .accept(ContentType.JSON)
                .log()
                .all(true)
        .when()
                .put("http://localhost:" + port + "/todos/" + id)
        .then()
                .log()
                .all(true)
                .and()
                .statusCode(is(404));
    }
}
