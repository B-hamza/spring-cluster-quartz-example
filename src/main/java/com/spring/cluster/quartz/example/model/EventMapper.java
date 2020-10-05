package com.spring.cluster.quartz.example.model;

import java.util.UUID;

import lombok.Value;

@Value
public class EventMapper {
  Long sequentialId;
  UUID eventId;
  UUID stateId;
}
