package ru.practicum.events.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class SearchDto {
    private List<Long> users;
    private List<String> states;
    private List<Long> categories;
    private String rangeStart;
    private String rangeEnd;
    private int from;
    private int size;
    private String text;
    private Boolean paid;
    private Boolean onlyAvailable;
    private String sort;

    public SearchDto (List<Long> users, List<String> states, List<Long> categories, String rangeStart, String rangeEnd,
                      int from, int size) {
        this.users = users;
        this.states = states;
        this.categories = categories;
        this.rangeStart = rangeStart;
        this.rangeEnd = rangeEnd;
        this.from = from;
        this.size = size;
    }

    public SearchDto(String text, List<Long> categories, Boolean paid, String rangeStart, String rangeEnd,
                     boolean onlyAvailable, String sort, int from, int size) {
        this.text = text;
        this.categories = categories;
        this.paid = paid;
        this.rangeEnd = rangeEnd;
        this.rangeStart = rangeStart;
        this.onlyAvailable = onlyAvailable;
        this.sort = sort;
        this.from = from;
        this.size = size;
    }
}
