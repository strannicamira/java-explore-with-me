package ru.practicum;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.Constants.SORT_BY_ID_ASC;
import static ru.practicum.Constants.TIME_PATTERN;

@Service
@RequiredArgsConstructor
@Slf4j
//@Transactional(readOnly = true)
public class StatServiceImpl implements StatService {
    private final StatRepository statRepository;

    @Override
    public EndpointHitDto post(EndpointHit endpointHit) {
        log.info("Create hit for request {} {}", endpointHit.getApp(), endpointHit.getUri());
        EndpointHitDto endpointHitDto = StatMapper.mapToEndpointHit(endpointHit);
        endpointHitDto = statRepository.save(endpointHitDto);

        return endpointHitDto;
    }

    @Override
    public Iterable<ViewStats> get(String start, String end, Boolean unique, String[] uris) {

        LocalDateTime startLDT = LocalDateTime.parse(URLDecoder.decode(start), DateTimeFormatter.ofPattern(TIME_PATTERN));
        LocalDateTime endtLDT = LocalDateTime.parse(URLDecoder.decode(end), DateTimeFormatter.ofPattern(TIME_PATTERN));
        List<String> urisList = null;
        if (uris != null) {
            urisList = Arrays.stream(uris).map(uri -> URLDecoder.decode(uri)).collect(Collectors.toList());
        }
        return getByUrisList(startLDT, endtLDT, unique, urisList);
    }

    private Iterable<ViewStats> getByUrisList(LocalDateTime start, LocalDateTime end, Boolean unique, List<String> uris) {
        //TODO: use count() or countDistinct?
        //NumberExpression<Long> longNumberExpression = QEndpointHitDto.endpointHitDto.uri.countDistinct();

        BooleanExpression byTimestamp = QEndpointHitDto.endpointHitDto.timestamp.between(start, end);
        BooleanExpression byUris = null;
        if (uris != null) {
            byUris = QEndpointHitDto.endpointHitDto.uri.in(uris);
        }
        Iterable<EndpointHitDto> endpointHitsDtos = statRepository.findAll(byTimestamp.and(byUris), SORT_BY_ID_ASC);
        return StatMapper.mapToViewStats(endpointHitsDtos, unique);
    }
}