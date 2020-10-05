package com.spring.cluster.quartz.example.datawriter;

import java.util.UUID;

import com.spring.cluster.quartz.example.model.EventMapper;
import com.spring.cluster.quartz.example.repository.EventRepository;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EventWriterJob implements Job {

  Logger log = LoggerFactory.getLogger(EventWriterJob.class);
  @Autowired EventRepository repository;

  @Override
  public void execute(JobExecutionContext context) throws JobExecutionException {
    log.info("writing event");
    EventMapper event = new EventMapper(null, UUID.randomUUID(), UUID.randomUUID());
    this.repository.save(event);
  }
  
}
