package ru.practicum.users.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.users.dto.UserDto;
import ru.practicum.users.service.UserService;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/users")
@Validated
public class AdminUserController {
    private final UserService userService;

    @Autowired
    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Collection<UserDto>> getUsers(@RequestParam(required = false) List<Long> ids,
                                                        @RequestParam(defaultValue = "0") int from,
                                                        @RequestParam(defaultValue = "10") int size) {
        return new ResponseEntity<>(userService.search(ids, from, size), HttpStatus.OK);
    }
}
