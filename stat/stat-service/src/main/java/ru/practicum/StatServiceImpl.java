package ru.practicum;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exceptionhandler.IllegalDataException;

import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.Constants.TIME_PATTERN;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatServiceImpl implements StatService {
    private final StatRepository statRepository;

    @Override
    @Transactional
    public EndpointHit post(NewEndpointHitRequest newEndpointHitRequest) {
        log.info("Create hit for request {} {}", newEndpointHitRequest.getApp(), newEndpointHitRequest.getUri());
        EndpointHit endpointHit = StatMapper.mapToEndpointHit(newEndpointHitRequest);
        endpointHit = statRepository.save(endpointHit);

        return endpointHit;
    }

    @Override
    public List<ViewStats> get(String start, String end, Boolean unique, String[] uris) {
        log.info("Get hit for range from {} to {}", start, end);

        LocalDateTime startLDT = LocalDateTime.parse(URLDecoder.decode(start), DateTimeFormatter.ofPattern(TIME_PATTERN));
        LocalDateTime endtLDT = LocalDateTime.parse(URLDecoder.decode(end), DateTimeFormatter.ofPattern(TIME_PATTERN));

        if (endtLDT.isBefore(startLDT)) {
            throw new IllegalDataException("End is before start");
        }

        List<String> urisList = null;
        if (uris != null) {
            urisList = Arrays.stream(uris).map(URLDecoder::decode).collect(Collectors.toList());
        }
        List<ViewStats> views = getByUrisList(startLDT, endtLDT, unique, urisList);
        Collections.sort(views, Comparator.comparing(ViewStats::getHits).reversed());

        return views;
    }

    private List<ViewStats> getByUrisList(LocalDateTime start, LocalDateTime end, Boolean unique, List<String> uris) {
        log.info("Get list of hits for range from {} to {}", start, end);

        BooleanExpression byTimestamp = QEndpointHit.endpointHit.timestamp.between(start, end);
        BooleanExpression byUris = null;
        if (uris != null) {
            byUris = QEndpointHit.endpointHit.uri.in(uris);
        }
        Iterable<EndpointHit> endpointHitsDtos = statRepository.findAll(byTimestamp.and(byUris));

        List<ViewStats> viewStats = StatMapper.mapToViewStats(endpointHitsDtos, unique);
        return viewStats;
    }
}
