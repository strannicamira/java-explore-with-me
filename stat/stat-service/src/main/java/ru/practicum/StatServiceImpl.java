package ru.practicum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
//@Transactional(readOnly = true)
public class StatServiceImpl implements StatService {
    private final StatRepository statRepository;
    @Override
    public EndpointHit create(EndpointHit endpointHit) {
        log.info("Create hit for {} {}", endpointHit.getApp(), endpointHit.getIp());
        endpointHit = statRepository.save(endpointHit);
        return endpointHit;
    }
}
