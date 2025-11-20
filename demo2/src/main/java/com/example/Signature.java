package com.example;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Signature {
    private final Task task;
    private final Object[] args;
    private final Map<String, Object> stamps;
    private final String id;

    public Signature(Task task, Object... args) {
        this.task = task;
        this.args = args;
        this.stamps = new HashMap<>();
        this.id = UUID.randomUUID().toString();
    }

    public Signature(Task task, Map<String, Object> stamps, Object... args) {
        this.task = task;
        this.args = args;
        this.stamps = new HashMap<>(stamps);
        this.id = UUID.randomUUID().toString();
    }

    public Object apply() {
        return task.apply(args);
    }

    public void stamp(String key, Object value) {
        stamps.put(key, value);
    }

    public Object getStamp(String key) {
        return stamps.get(key);
    }

    public Map<String, Object> getStamps() {
        return new HashMap<>(stamps);
    }

    public boolean hasStamp(String key) {
        return stamps.containsKey(key);
    }

    public Task getTask() {
        return task;
    }

    public Object[] getArgs() {
        return args.clone();
    }

    public String getId() {
        return id;
    }

    public Signature clone() {
        Signature cloned = new Signature(task, new HashMap<>(stamps), args);
        return cloned;
    }
}

