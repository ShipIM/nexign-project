package com.example.commutator.api.converter;

public interface Converter<T, E> {

    T convertTo(E element);

    E convertFrom(T element);

}
