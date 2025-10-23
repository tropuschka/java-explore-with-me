package ru.practicum.compilations.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.compilations.dto.CompilationDto;
import ru.practicum.compilations.dto.NewCompilationDto;
import ru.practicum.compilations.dto.UpdateCompilationRequest;
import ru.practicum.compilations.mapping.CompilationMapper;
import ru.practicum.compilations.model.Compilation;
import ru.practicum.compilations.repository.CompilationRepository;
import ru.practicum.events.model.Event;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.users.model.User;
import ru.practicum.users.repository.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    public CompilationDto addCompilation(Long adminId, NewCompilationDto newCompilationDto) {
        checkAdmin(adminId);
        Compilation compilation = CompilationMapper.toCompilation(newCompilationDto);

        if (newCompilationDto.getEvents() != null && !newCompilationDto.getEvents().isEmpty()) {
            List<Event> compilationEvents = checkEvents(newCompilationDto.getEvents());
            compilation.setEvents(new HashSet<>(compilationEvents));
        } else compilation.setEvents(new HashSet<>());

        Compilation saved = compilationRepository.save(compilation);
        return CompilationMapper.toDto(saved);
    }

    @Override
    public void deleteCompilation(Long adminId, Long compilationId) {
        checkAdmin(adminId);
        findCompilation(compilationId);
        compilationRepository.deleteById(compilationId);
    }

    @Override
    public CompilationDto updateCompilation(Long adminId, Long id, UpdateCompilationRequest request) {
        checkAdmin(adminId);
        Compilation compilation = findCompilation(id);
        if (request.getEvents() != null) {
            List<Event> compilationEvents = checkEvents(request.getEvents());
            compilation.setEvents(new HashSet<>(compilationEvents));
        }
        if (request.getPinned() != null) compilation.setPinned(request.getPinned());
        if (request.getTitle() != null) compilation.setTitle(request.getTitle());
        Compilation saved = compilationRepository.save(compilation);
        return CompilationMapper.toDto(saved);
    }

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, int from, int size) {
        Page<Compilation> compilations;
        if (pinned == null) {
            compilations = compilationRepository.findAll(PageRequest.of(from, size));
        } else {
            compilations = compilationRepository.findByPinned(pinned, PageRequest.of(from, size));
        }
        return compilations.stream().map(CompilationMapper::toDto).toList();
    }

    @Override
    public CompilationDto getCompilationById(Long compId) {
        Compilation compilation = findCompilation(compId);
        return CompilationMapper.toDto(compilation);
    }

    private Compilation findCompilation(Long id) {
        Optional<Compilation> check = compilationRepository.findById(id);
        if (check.isPresent()) return check.get();
        else throw new NotFoundException("Подборка не найдена");
    }

    private List<Event> checkEvents(Set<Long> events) {
        List<Event> compilationEvents = eventRepository.findAllById(events);
        if (compilationEvents.size() != events.size()) {
            throw new NotFoundException("Найдены не все события");
        }
        return compilationEvents;
    }

    private void checkAdmin(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) throw new NotFoundException("Пользователь не найден");
    }
}
