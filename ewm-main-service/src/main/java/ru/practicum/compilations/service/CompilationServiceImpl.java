package ru.practicum.compilations.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.compilations.dto.CompilationDto;
import ru.practicum.compilations.dto.NewCompilationDto;
import ru.practicum.compilations.mapping.CompilationMapper;
import ru.practicum.compilations.model.Compilation;
import ru.practicum.compilations.repository.CompilationRepository;
import ru.practicum.events.model.Event;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.exceptions.NotFoundException;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Override
    public CompilationDto addCompilation(NewCompilationDto newCompilationDto) {
        Compilation compilation = CompilationMapper.toCompilation(newCompilationDto);

        if (newCompilationDto.getEvents() != null && !newCompilationDto.getEvents().isEmpty()) {
            List<Event> compilationEvents = eventRepository.findAllById(newCompilationDto.getEvents());
            if (compilationEvents.size() != newCompilationDto.getEvents().size()) {
                throw new NotFoundException("Найдены не все события");
            }
            compilation.setEvents(new HashSet<>(compilationEvents));
        } else compilation.setEvents(new HashSet<>());

        Compilation saved = compilationRepository.save(compilation);
        return CompilationMapper.toDto(saved);
    }

    @Override
    public void deleteCompilation(Long compilationId) {
        Optional<Compilation> check = compilationRepository.findById(compilationId);
        if (check.isPresent()) compilationRepository.deleteById(compilationId);
        else throw new NotFoundException("Подборка не найдена");
    }
}
