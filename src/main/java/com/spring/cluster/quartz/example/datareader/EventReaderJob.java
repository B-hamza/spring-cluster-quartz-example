package com.spring.cluster.quartz.example.datareader;

import com.spring.cluster.quartz.example.model.EventMapper;
import com.spring.cluster.quartz.example.repository.EventRepository;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class EventReaderJob implements Job {

  Logger log = LoggerFactory.getLogger(EventReaderJob.class);
  @Autowired EventRepository repository;

  @Override
  public void execute(JobExecutionContext context) throws JobExecutionException {
    log.info("reading event");
    final JobDataMap dataMap = context.getJobDetail().getJobDataMap();
    final Long lastSavedEventIndex = dataMap.getLong("lastEventIndex");
    final int batchSize = 10;
    var events = repository.selectNextToProcess(batchSize, lastSavedEventIndex);
    events.forEach(event -> log.info("event logging {}", event.getSequentialId()));
    final Long newEventIndex = events.stream()
      .map(EventMapper::getSequentialId)
      .reduce((first, second) -> second)
      .orElse(lastSavedEventIndex);
    dataMap.put("lastEventIndex", newEventIndex);
  }
  
}
