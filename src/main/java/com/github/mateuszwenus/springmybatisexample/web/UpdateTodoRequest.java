package com.github.mateuszwenus.springmybatisexample.web;

public class UpdateTodoRequest {
    private String title;
    private String text;
    private Integer version;

    public UpdateTodoRequest() {
    }

    public UpdateTodoRequest(String title, String text, Integer version) {
        this.title = title;
        this.text = text;
        this.version = version;
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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
