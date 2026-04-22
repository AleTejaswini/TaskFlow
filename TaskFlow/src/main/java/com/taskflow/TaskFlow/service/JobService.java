package com.taskflow.TaskFlow.service;

import com.taskflow.TaskFlow.entity.ScheduledJob;
import com.taskflow.TaskFlow.entity.ScheduledJobStatus;
import com.taskflow.TaskFlow.repository.JobRepository;
import com.taskflow.TaskFlow.scheduler.EmailJob;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class JobService {

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private JobRepository jobRepository;

    public String createJob(String name, String group, String cron, String email) throws SchedulerException {

        
        JobDetail jobDetail = JobBuilder.newJob(EmailJob.class)
            .withIdentity(name, group)
            .usingJobData("email", email)
            .storeDurably()
            .build();

        
        CronTrigger trigger = TriggerBuilder.newTrigger()
            .withIdentity(name + "_trigger", group)
            .withSchedule(CronScheduleBuilder.cronSchedule(cron))
            .build();

       
        scheduler.scheduleJob(jobDetail, trigger);

      
        ScheduledJob job = new ScheduledJob();
        job.setJobName(name);
        job.setJobGroup(group);
        job.setCronExpression(cron);
        job.setEmail(email);
        job.setStatus(ScheduledJobStatus.SCHEDULED);
        jobRepository.save(job);

        return "Job scheduled: " + name + " [" + cron + "] → " + email;
    }

    @Transactional
    public void pauseJob(String name, String group) throws SchedulerException {
        scheduler.pauseJob(JobKey.jobKey(name, group));

        jobRepository.findByJobNameAndJobGroup(name, group).ifPresent(job -> {
            job.setStatus(ScheduledJobStatus.PAUSED);
            jobRepository.save(job);
        });
    }

    @Transactional
    public void resumeJob(String name, String group) throws SchedulerException {
        scheduler.resumeJob(JobKey.jobKey(name, group));

        jobRepository.findByJobNameAndJobGroup(name, group).ifPresent(job -> {
            job.setStatus(ScheduledJobStatus.SCHEDULED);
            jobRepository.save(job);
        });
    }

    @Transactional
    public void deleteJob(String name, String group) throws SchedulerException {
        scheduler.deleteJob(JobKey.jobKey(name, group));
        jobRepository.deleteByJobNameAndJobGroup(name, group);
    }

    public List<ScheduledJob> getAllJobs() {
        return jobRepository.findAll();
    }
}
