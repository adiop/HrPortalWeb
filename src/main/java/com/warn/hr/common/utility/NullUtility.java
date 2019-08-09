package com.warn.hr.common.utility;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public interface NullUtility {

    default <T> void whenNotNull(T t, Consumer<T> consumer) {
        consume((v) -> Objects.nonNull(v), t, consumer);
    }


    default void whenNotNullOrEmpty(String t, Consumer<String> consumer) {
        consume((v) -> Objects.nonNull(v) && !v.isEmpty(), t, consumer);

    }

    default <T> void whenNotNullOrEmpty(List<T> t, Consumer<List<T>> consumer) {
        consume((v) -> Objects.nonNull(v) && !v.isEmpty(), t, consumer);
    }

    default <T> T whenNull(T t, Supplier<T> supplier) {
        return supply((v) -> Objects.isNull(v), t, supplier);
    }


    default String whenNullOrEmpty(String t, Supplier<String> supplier) {
        return supply((v) -> Objects.isNull(v) && v.isEmpty(), t, supplier);

    }

    default <T> List<T> whenNullOrEmpty(List<T> t, Supplier<List<T>> supplier) {
        return supply((v) -> Objects.isNull(v) && v.isEmpty(), t, supplier);
    }

    default <T> T getFromNullable(T value, T defaultValue) {
        return Optional.ofNullable(value).orElseGet(() -> defaultValue);
    }

    default <T, V> V getFromNullable(T value, Function<T, V> function) {
        return Optional.ofNullable(value).filter(v -> v != null).map(function).orElseGet(() -> null);
    }

    default <T, V> V getFromNullable(T value, Function<T, V> function, V defaultValue) {
        return Optional.ofNullable(value).filter(v -> v != null).map(function).orElseGet(() -> defaultValue);
    }

    default <T> void consume(Predicate<T> predicate, T t, Consumer<T> consumer) {

        if(predicate.test(t))
            consumer.accept(t);

    }

    default <T> T supply(Predicate<T> predicate, T t, Supplier<T> supplier) {

        if(predicate.test(t))
            t = supplier.get();

        return t;

    }

}
