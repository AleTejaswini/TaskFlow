package com.taskflow.TaskFlow.scheduler;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

//public class EmailJob implements Job {
//
//    private static final Logger log = LoggerFactory.getLogger(EmailJob.class);
//
//    @Override
//    public void execute(JobExecutionContext context) throws JobExecutionException {
//        String email = context.getMergedJobDataMap().getString("email");
//        String jobName = context.getJobDetail().getKey().getName();
//
//      
//        log.info("[TaskFlow] Job '{}' fired at {}. Sending email to: {}", jobName, LocalDateTime.now(), email);
//
//      
//    }
//}



public class EmailJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        // Get email 
        String email = context.getMergedJobDataMap().getString("email");

        // Get job name
        String jobName = context.getJobDetail().getKey().getName();

        // Print output
        System.out.println("[TaskFlow] Job '" + jobName + 
                "' fired at " + LocalDateTime.now() + 
                ". Sending email to: " + email);
    }
}
