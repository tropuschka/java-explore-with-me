package ru.practicum.client;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.dto.ResponseStatDto;
import ru.practicum.dto.StatDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component
public class StatClient {

    private final String path = "http://localhost:9090";
    private final RestTemplate restTemplate;

    public StatClient() {
        this.restTemplate = new RestTemplateBuilder()
                .uriTemplateHandler(new DefaultUriBuilderFactory(path))
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                .build();
    }

    public void saveStat(StatDto statDto) {
        restTemplate.postForLocation(path + "/hit", statDto);
    }

    public List<ResponseStatDto> getViewStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        String formattedStart = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(start);
        String formattedEnd = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(end);

        ResponseEntity<ResponseStatDto[]> responseResult = restTemplate.getForEntity(
                path + "/stats" + "?start=" + formattedStart + "&end=" + formattedEnd +
                        "&uris=" + String.join(",", uris) + "&unique=" + unique,
                ResponseStatDto[].class);

        return Arrays.asList(Objects.requireNonNull(responseResult.getBody()));
    }
}
