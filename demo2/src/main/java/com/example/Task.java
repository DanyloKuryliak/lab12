package com.example;

public interface Task {
    Object apply(Object... args);
    String getName();
}

