package ru.practicum;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.stream.Collectors;

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
        return post(endpointHitDto);
    }

    @Override
    public EndpointHitDto post(EndpointHitDto endpointHitDto) {
        log.info("Create hit for {} {}", endpointHitDto.getApp(), endpointHitDto.getIp());
        endpointHitDto = statRepository.save(endpointHitDto);
        return endpointHitDto;
    }

    @Override
    public Iterable<ViewStats> get(LocalDateTime start, LocalDateTime end, ArrayList<String> uris, Boolean unique) {
        BooleanExpression byBetween = QEndpointHitDto.endpointHitDto.timestamp.between(start, end);
        BooleanExpression byUris = QEndpointHitDto.endpointHitDto.uri.in(uris);//TODO: or use array[string] as param as well?

        Iterable<EndpointHitDto> endpointHits = statRepository.findAll(byBetween.and(byUris));

        return StatMapper.mapToViewStats(endpointHits, unique);
    }


    @Override
    public Iterable<ViewStats> get(String start, String end, ArrayList<Integer> uris, Boolean unique) {
        ArrayList<String> urisString = (ArrayList<String>) uris.stream().map(uri -> "/events/" + uri).collect(Collectors.toList());
        return getByUris(start, end, urisString, unique);
    }

    @Override
    public Iterable<ViewStats> getByUris(String start, String end, ArrayList<String> uris, Boolean unique) {

        LocalDateTime startTimestamp = LocalDateTime.parse(URLDecoder.decode(start), DateTimeFormatter.ofPattern(TIME_PATTERN));
        LocalDateTime endtTimestamp = LocalDateTime.parse(URLDecoder.decode(end), DateTimeFormatter.ofPattern(TIME_PATTERN));

        BooleanExpression byBetween = QEndpointHitDto.endpointHitDto.timestamp.between(startTimestamp, endtTimestamp);
        //TODO: use array[string] as param for uris?
        BooleanExpression byUris = QEndpointHitDto.endpointHitDto.uri.in(uris);

        //TODO: use count() or countDistinct?
//        NumberExpression<Long> longNumberExpression = QEndpointHitDto.endpointHitDto.uri.countDistinct();

        Iterable<EndpointHitDto> endpointHits = statRepository.findAll(byBetween.and(byUris));

        return StatMapper.mapToViewStats(endpointHits, unique);
    }

}
