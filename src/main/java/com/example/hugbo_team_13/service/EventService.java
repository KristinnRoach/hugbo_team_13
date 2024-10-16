package com.example.hugbo_team_13.service;


import com.example.hugbo_team_13.model.EventDTO;
import com.example.hugbo_team_13.persistence.entity.EventEntity;
import com.example.hugbo_team_13.persistence.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public EventDTO createEvent(EventDTO eventDTO) {
        // Todo: Validate input
        String name = eventDTO.getName();
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Event name cannot be empty");
        }
        if (eventRepository.existsByName(name)) {
            // logger.warn("Attempted to create event with existing event name: {}", name);
            throw new RuntimeException("Event name already exists");
        }

        // Create EventEntity
        EventEntity event = new EventEntity();
        event.setName(eventDTO.getName());
        event.setStartTime(eventDTO.getStartDate());
        event.setEndTime(eventDTO.getEndDate());
        
        EventEntity savedEvent = eventRepository.save(event);

        // Return a new EventDTO
        return convertToDTO(savedEvent);
    }

    public Optional<EventDTO> getEventById(Long id) {
        return eventRepository.findById(id).map(this::convertToDTO);
    }

    public EventDTO getEventByName(String name) {
        EventEntity event = eventRepository.findByName(name);
        return event != null ? convertToDTO(event) : null;
    }

    public List<EventDTO> getAllEvents() {
        List<EventEntity> eventEntities = eventRepository.findAll();
        List<EventDTO> eventDTOs = new ArrayList<>();

        for (EventEntity event : eventEntities) {
            eventDTOs.add(convertToDTO(event));
        }
        return eventDTOs;
    }


    public EventDTO saveEvent(EventDTO eventDTO) { // (save creates a new entity if ID is not set)
        EventEntity event = convertToEntity(eventDTO);
        EventEntity savedEvent = eventRepository.save(event);
        return convertToDTO(savedEvent);
    }

    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }

    public void deleteAllEvents() {
        eventRepository.deleteAll();
    }

    private EventDTO convertToDTO(EventEntity event) {
        return new EventDTO(event.getId(), event.getName(), event.getStartTime(), event.getEndTime());
    }

    private EventEntity convertToEntity(EventDTO eventDTO) {
        EventEntity event = new EventEntity(eventDTO.getName());
        event.setId(eventDTO.getId());
        return event;
    }

}
