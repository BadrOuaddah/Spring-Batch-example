package com.example.springBatchExample;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

// TODO: Parameter 2 of method step1 in com.example.springBatchExample.CsvToDatabaseJob required a bean of type 'org.springframework.batch.item.ItemProcessor' that could not be found.
@SpringBootApplication
@EnableBatchProcessing
public class BatchProcessingApplication implements CommandLineRunner {

    private final JobLauncher jobLauncher;
    private final ApplicationContext applicationContext;

    public BatchProcessingApplication(JobLauncher jobLauncher, ApplicationContext applicationContext) {
        this.jobLauncher = jobLauncher;
        this.applicationContext = applicationContext;
    }

    public static void main(String[] args) {
        SpringApplication.run(BatchProcessingApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Job job = (Job) applicationContext.getBean("insertIntoDbFromCsvJob");

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("JobID", String.valueOf(System.currentTimeMillis()))
                .toJobParameters();

        var jobExecution = jobLauncher.run(job, jobParameters);

        var batchStatus = jobExecution.getStatus();
        while (batchStatus.isRunning()) {
            System.out.println("Still running...");
            Thread.sleep(5000L);
        }

    }
}
