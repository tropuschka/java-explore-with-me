package ru.practicum.client;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
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
    private final RestTemplate restTemplate;
    private static final String timeFormat = "yyyy-MM-dd HH:mm:ss";

    public StatClient(@Value("${stats-server.url}") String path) {
        this.restTemplate = new RestTemplateBuilder()
                .uriTemplateHandler(new DefaultUriBuilderFactory(path))
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                .build();
    }

    public void saveStat(HttpServletRequest request, String application) {
        StatDto statDto = new StatDto(null, application, request.getRemoteAddr(), request.getRequestURI(),
                LocalDateTime.now().toString());
        restTemplate.postForLocation("/hit", statDto);
    }

    public List<ResponseStatDto> getViewStats(LocalDateTime start, LocalDateTime end,
                                              List<String> uris, Boolean unique) {
        String formattedStart = DateTimeFormatter.ofPattern(timeFormat).format(start);
        String formattedEnd = DateTimeFormatter.ofPattern(timeFormat).format(end);

        ResponseEntity<ResponseStatDto[]> responseResult = restTemplate.getForEntity(
                "/stats" + "?start=" + formattedStart + "&end=" + formattedEnd +
                        "&uris=" + String.join(",", uris) + "&unique=" + unique,
                ResponseStatDto[].class);

        return Arrays.asList(Objects.requireNonNull(responseResult.getBody()));
    }
}
