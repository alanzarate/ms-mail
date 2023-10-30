package com.ucb.edu.msmail.repository;

import com.ucb.edu.msmail.entity.EventEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface EventRepository extends PagingAndSortingRepository<EventEntity, Integer> {

    EventEntity findByEventId(Integer eventId);
}
