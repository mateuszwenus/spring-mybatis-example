package com.github.mateuszwenus.springmybatisexample.service;

import java.util.UUID;

public record UpdateTodoCmd (UUID id, String title, String text, int version) {
}
