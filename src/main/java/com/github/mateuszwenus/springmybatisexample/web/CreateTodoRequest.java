package com.github.mateuszwenus.springmybatisexample.web;

public class CreateTodoRequest {
    private String title;
    private String text;

    public CreateTodoRequest() {
    }

    public CreateTodoRequest(String title, String text) {
        this.title = title;
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
