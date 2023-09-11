package ru.practicum.util;

import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Sort;

@UtilityClass
public class Constants {
    public static final String TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final Sort SORT_BY_ID_ASC = Sort.by(Sort.Direction.ASC, "id");
    public static final Sort SORT_BY_ID_DESC = Sort.by(Sort.Direction.DESC, "id");
    public static final Sort SORT_BY_START_DESC = Sort.by(Sort.Direction.DESC, "start");
    public static final Sort SORT_BY_REQUEST_CREATED_DESC = Sort.by(Sort.Direction.DESC, "created");

    public static final Sort SORT_BY_EVENT_DATE_ASC = Sort.by(Sort.Direction.ASC, "eventDate");
    public static final Sort SORT_BY_VIEWS_ASC = Sort.by(Sort.Direction.ASC, "views");

    public static final Integer PAGE_SIZE = 10;
    public static final Integer MAGIC_NUMBER = 999;

}