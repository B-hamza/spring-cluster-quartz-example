package com.spring.cluster.quartz.example.configuration;

import com.spring.cluster.quartz.example.datareader.EventReaderJob;
import com.spring.cluster.quartz.example.datawriter.EventWriterJob;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;

@Component
public class EventProcessor {

  @Bean(name = "readerJob")
  public JobDetail eventReaderJob() {
    
    return JobBuilder.newJob().ofType(EventReaderJob.class)
      .storeDurably()
      .usingJobData("lastEventIndex", 0L)
      .withIdentity("reader1", "event_handlers")  
      .withDescription("read events each second")
      .build();
  }

  @Bean(name = "writerJob")
  public JobDetail eventWriterJob() {
    return JobBuilder.newJob().ofType(EventWriterJob.class)
      .storeDurably()
      .withIdentity("reader2", "event_writers")  
      .withDescription("write events each second")
      .build();
  }

  @Bean
  public Trigger triggerReader(JobDetail readerJob) {
    return TriggerBuilder.newTrigger()
      .forJob(readerJob)
      .withIdentity("reader_trigger")
      .withSchedule(simpleSchedule().repeatForever().withIntervalInSeconds(1))
      .build();
  }

  @Bean
  public Trigger triggerWriter(JobDetail writerJob) {
    return TriggerBuilder.newTrigger()
      .forJob(writerJob)
      .withIdentity("writer_trigger")
      .withSchedule(simpleSchedule().repeatForever().withIntervalInSeconds(1))
      .build();
  }
  
}
