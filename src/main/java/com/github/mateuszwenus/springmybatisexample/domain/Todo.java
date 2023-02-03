package com.github.mateuszwenus.springmybatisexample.domain;

import java.util.UUID;

public record Todo(UUID id, String title, String text) {
}
