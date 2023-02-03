package com.github.mateuszwenus.springmybatisexample.web;

import com.github.mateuszwenus.springmybatisexample.service.CreataTodoCmd;
import com.github.mateuszwenus.springmybatisexample.service.TodoNotFoundException;
import com.github.mateuszwenus.springmybatisexample.service.TodoService;
import com.github.mateuszwenus.springmybatisexample.service.UpdateTodoCmd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class TodoController {

    private final TodoService todoService;

    @Autowired
    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping("/todos")
    public List<TodoListDto> getTodoList() {
        return todoService.findAll()
                .stream()
                .map(TodoListDto::new)
                .toList();
    }

    @GetMapping("/todos/{id}")
    public TodoDto getTodoList(@PathVariable UUID id) {
        return new TodoDto(todoService.findById(id));
    }

    @PutMapping("/todos/{id}")
    public TodoDto updateTodo(@PathVariable UUID id, @RequestBody UpdateTodoRequest req) {
        return new TodoDto(todoService.updateTodo(new UpdateTodoCmd(id, req.getTitle(), req.getText())));
    }

    @PostMapping("/todos")
    public TodoDto createTodo(@RequestBody CreateTodoRequest req) {
        return new TodoDto(todoService.createTodo(new CreataTodoCmd(req.getTitle(), req.getText())));
    }

    @ExceptionHandler
    public ResponseEntity<Void> handleTodoNotFoundException(TodoNotFoundException e) {
        return ResponseEntity.notFound().build();
    }
}
