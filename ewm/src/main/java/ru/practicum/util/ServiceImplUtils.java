package ru.practicum.util;

import lombok.experimental.UtilityClass;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

import static ru.practicum.util.Constants.PAGE_SIZE;

@UtilityClass
public class ServiceImplUtils {
    public static Pageable getPage(Integer from, Integer size, Sort sort) {
        Pageable page = PageRequest.of(0, PAGE_SIZE, sort);
        if (from != null && size != null) {

            if (from < 0 || size <= 0) {
                throw new IllegalStateException("Not correct page parameters");
            }
            page = PageRequest.of(from > 0 ? from / size : 0, size, sort);
        }
        return page;
    }

    public static <T> List<T> mapToList(Iterable<T> obgs) {
        List<T> list = new ArrayList<>();
        for (T obg : obgs) {
            list.add(obg);
        }
        return list;
    }
}