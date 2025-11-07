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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Override
    public CompilationDto addCompilation(NewCompilationDto newCompilationDto) {
        Compilation compilation = CompilationMapper.toCompilation(newCompilationDto);

        if (newCompilationDto.getEvents() != null && !newCompilationDto.getEvents().isEmpty()) {
            List<Event> compilationEvents = checkEvents(newCompilationDto.getEvents());
            compilation.setEvents(new HashSet<>(compilationEvents));
        } else compilation.setEvents(new HashSet<>());

        Compilation saved = compilationRepository.save(compilation);
        return CompilationMapper.toDto(saved);
    }

    @Override
    public void deleteCompilation(Long compilationId) {
        findCompilation(compilationId);
        compilationRepository.deleteById(compilationId);
    }

    @Override
    public CompilationDto updateCompilation(Long id, UpdateCompilationRequest request) {
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
        return compilationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Подборка с ID " + id + " не найдена"));
    }

    private List<Event> checkEvents(Set<Long> events) {
        List<Event> compilationEvents = eventRepository.findAllById(events);
        if (compilationEvents.size() != events.size()) {
            throw new NotFoundException("Найдены не все события");
        }
        return compilationEvents;
    }
}
