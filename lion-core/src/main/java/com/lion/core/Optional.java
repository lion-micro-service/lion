package com.lion.core;

/**
 * 用于duboo序列化-专用
 */

import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.Objects;

public class Optional<T> implements Serializable {
    private static final long serialVersionUID = -90000051L;
    private static final com.lion.core.Optional<?> EMPTY = new com.lion.core.Optional<>(null);

    private T value;


    public Optional() {
    }

    public static<T> Optional<T> empty() {
        com.lion.core.Optional<T> o = (com.lion.core.Optional<T>) EMPTY;
        return o;
    }

    public Optional(java.util.Optional<T> optional) {
        this.value = Objects.nonNull(optional)? (optional.isPresent()?optional.get():null):null;
    }

    public T get() {
        if (value == null) {
            throw new NoSuchElementException("No value present");
        }
        return value;
    }

    public boolean isPresent() {
        return Objects.nonNull(value);
    }

    public boolean isEmpty() {
        return Objects.isNull(value);
    }
}
