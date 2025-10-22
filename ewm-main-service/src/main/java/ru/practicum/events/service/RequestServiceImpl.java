package ru.practicum.events.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.events.dto.participation.ParticipationRequestDto;
import ru.practicum.events.mapping.RequestMapper;
import ru.practicum.events.model.Request;
import ru.practicum.events.repository.RequestRepository;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.users.model.User;
import ru.practicum.users.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;

    @Override
    public List<ParticipationRequestDto> getUserRequests (Long userId) {
        checkUser(userId);
        List<Request> requests = requestRepository.findByParticipantId(userId);
        return requests.stream().map(RequestMapper::toDto).toList();
    }

    private void checkUser(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) throw new NotFoundException("Пользователь не найден");
    }
}
