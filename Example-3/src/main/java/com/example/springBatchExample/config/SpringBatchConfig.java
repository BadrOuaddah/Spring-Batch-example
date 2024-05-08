package com.example.springBatchExample.config;

import com.example.springBatchExample.entity.Student;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

@Configuration
@EnableBatchProcessing
public class SpringBatchConfig {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Bean
    public FlatFileItemReader<Student> readStudents() {
        return new FlatFileItemReaderBuilder<Student>()
                .name("readStudents")
                .resource(new ClassPathResource("student.csv"))
                .linesToSkip(1)
                .delimited()
                .names(new String[]{"id", "firstname", "lastname", "age", "gender", "dateOfBirthday", "email", "score", "status"})
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                    setTargetType(Student.class);
                }})
                .build();
    }

    @Bean
    public StudentProcess studentProcess() {
        return new StudentProcess();
    }

    @Bean
    public FlatFileItemWriter<Student> writReceiversStudents(@Value("${source-output}") String output) {
        return new FlatFileItemWriterBuilder<Student>()
                .name("writeStudents")
                .resource(new FileSystemResource(output))
                .append(false)
                .delimited()
                .names(new String[]{"id", "firstname", "lastname", "age", "gender", "dateOfBirthday", "email", "score", "status"})
                .build();
    }


    @Bean
    public Step step(@Value("${source-output}") String output) {
        return stepBuilderFactory.get("step")
                .<Student, Student>chunk(5)
                .reader(readStudents())
                .processor(studentProcess())
                .writer(writReceiversStudents(output))
                .build();
    }

    @Bean
    public Job job1(Step step) {

        return jobBuilderFactory.get("importUserJob")
                .incrementer(new RunIdIncrementer())
                .flow(step)
                .end()
                .build();
    }

}
