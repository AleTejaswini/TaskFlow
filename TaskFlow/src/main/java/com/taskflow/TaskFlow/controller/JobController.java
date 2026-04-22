package com.taskflow.TaskFlow.controller;

import com.taskflow.TaskFlow.dto.JobRequest;
import com.taskflow.TaskFlow.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/jobs")
public class JobController {

    @Autowired
    private JobService jobService;

    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody JobRequest request) throws Exception {
        String result = jobService.createJob(
            request.getName(),
            request.getGroup(),
            request.getCron(),
            request.getEmail()
        );
        return ResponseEntity.ok(result);
    }

    @PutMapping("/pause/{jobName}/{jobGroup}")
    public ResponseEntity<String> pause(@PathVariable String jobName,
                                        @PathVariable String jobGroup) throws Exception {
        jobService.pauseJob(jobName, jobGroup);
        return ResponseEntity.ok("Job paused: " + jobName);
    }

    @PutMapping("/resume/{jobName}/{jobGroup}")
    public ResponseEntity<String> resume(@PathVariable String jobName,
                                         @PathVariable String jobGroup) throws Exception {
        jobService.resumeJob(jobName, jobGroup);
        return ResponseEntity.ok("Job resumed: " + jobName);
    }

    @DeleteMapping("/delete/{jobName}/{jobGroup}")
    public ResponseEntity<String> delete(@PathVariable String jobName,
                                         @PathVariable String jobGroup) throws Exception {
        jobService.deleteJob(jobName, jobGroup);
        return ResponseEntity.ok("Job deleted: " + jobName);
    }

    @GetMapping("/list")
    public ResponseEntity<?> listAll() {
        return ResponseEntity.ok(jobService.getAllJobs());
    }
}
