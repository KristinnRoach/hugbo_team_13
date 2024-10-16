package com.example.hugbo_team_13.service;


import com.example.hugbo_team_13.persistence.repository.EventRepository;
import org.springframework.stereotype.Service;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    //public EventEntity getEventById(Long id) {
    //  eventRepository.
    //}

}
