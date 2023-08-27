package ru.practicum;

import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Sort;

@UtilityClass
public class Constants {
    public static final String TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static final Sort SORT_BY_APP_DESC = Sort.by(Sort.Direction.DESC, "app");
    public static final Sort SORT_BY_APP_ASC = Sort.by(Sort.Direction.ASC, "app");

    public static final Sort SORT_BY_URI_ASC = Sort.by(Sort.Direction.ASC, "uri");
    public static final Sort SORT_BY_URI_DESC = Sort.by(Sort.Direction.DESC, "uri");

    public static final Sort SORT_BY_ID_DESC = Sort.by(Sort.Direction.DESC, "id");

    public static final Sort SORT_BY_ID_ASC = Sort.by(Sort.Direction.ASC, "id");

}