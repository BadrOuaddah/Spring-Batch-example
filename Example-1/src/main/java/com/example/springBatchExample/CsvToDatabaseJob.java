package com.example.springBatchExample;

import com.example.springBatchExample.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import javax.sql.DataSource;

@Configuration
public class CsvToDatabaseJob {

    public static final Logger logger = LoggerFactory.getLogger(CsvToDatabaseJob.class);
    private static final String INSERT_QUERY = """
            insert into person (first_name, last_name, age, is_active)
            values (:firstName,:lastName,:age,:active)""";

    private final JobRepository jobRepository;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    public CsvToDatabaseJob(JobRepository jobRepository, JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        this.jobRepository = jobRepository;
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Value("classpath:csv/person.csv")
    private Resource inputFeed;

    @Autowired
    private DataSource dataSource;

    @Bean
    public Job insertIntoDbFromCsvJob(Step step1) {
        return jobBuilderFactory.get("Persons Import Job")
                .incrementer(new RunIdIncrementer())
                .start(step1)
                .build();
    }

    @Bean
    public Step step1(ItemReader<Person> reader,
                      ItemWriter<Person> writer,
                      ItemProcessor<Person, Person> processor) {
        return stepBuilderFactory.get("INSERT CSV RECORDS To DB Step")
                .<Person, Person>chunk(5)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public FlatFileItemReader<Person> csvFileReader(LineMapper<Person> lineMapper) {
        FlatFileItemReader<Person> itemReader = new FlatFileItemReader<>();
        itemReader.setLineMapper(lineMapper);
        itemReader.setResource(inputFeed);
        return itemReader;
    }

    @Bean
    public DefaultLineMapper<Person> lineMapper(LineTokenizer tokenizer,
                                                FieldSetMapper<Person> mapper) {
        DefaultLineMapper<Person> lineMapper = new DefaultLineMapper<>();
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(mapper);
        return lineMapper;
    }

    @Bean
    public BeanWrapperFieldSetMapper<Person> fieldSetMapper() {
        BeanWrapperFieldSetMapper<Person> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Person.class);
        return fieldSetMapper;
    }

    @Bean
    public DelimitedLineTokenizer tokenizer() {
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setDelimiter(",");
        tokenizer.setNames("firstName", "lastName", "age", "active");
        return tokenizer;
    }

    @Bean
    public JdbcBatchItemWriter<Person> jdbcItemWriter() {
        JdbcBatchItemWriter<Person> itemWriter = new JdbcBatchItemWriter<>();
        itemWriter.setDataSource(dataSource);
        itemWriter.setSql(INSERT_QUERY);
        itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        return itemWriter;
    }

    @Bean
    public ItemProcessor<Person, Person> processor() {
        return item -> {
            // Perform any processing logic here if needed
            return item;
        };
    }
}
