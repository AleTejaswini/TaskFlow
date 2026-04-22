package com.taskflow.TaskFlow.repository;

import com.taskflow.TaskFlow.entity.ScheduledJob;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JobRepository extends JpaRepository<ScheduledJob, Long> {

    Optional<ScheduledJob> findByJobNameAndJobGroup(String jobName, String jobGroup);

    void deleteByJobNameAndJobGroup(String jobName, String jobGroup);
}
