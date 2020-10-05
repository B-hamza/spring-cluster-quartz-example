package com.spring.cluster.quartz.example.repository;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.spring.cluster.quartz.example.model.EventMapper;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class EventRepository {
  NamedParameterJdbcTemplate jdbcTemplate;

  public EventRepository(NamedParameterJdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public void save(EventMapper event) {
    String sql = "INSERT INTO events(event_id, state_id) VALUES (:eventId, :stateId)";
    var params = Map.of("eventId", event.getEventId(), "stateId", event.getStateId());
    jdbcTemplate.update(sql, params);
  }

  public List<EventMapper> selectNextToProcess(int batchSize, Long from) {
    String sql = "SELECT sequential_id, event_id, state_id FROM events where sequential_id > :from Order By sequential_id LIMIT :batchSize";
    var params = Map.of("batchSize", batchSize, "from", from);
    return jdbcTemplate.query(sql, params, (rs, index) -> {
      return new EventMapper(
        rs.getLong("sequential_id"), 
        UUID.fromString(rs.getString("event_id")), 
        UUID.fromString(rs.getString("state_id"))
        );
    });
  }

}
