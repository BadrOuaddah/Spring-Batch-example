# Spring Batch example

### Prerequisites to start spring batch example 1 :

- H2 Database :
``` xml
        <dependency>
            <groupId>com.h2database</groupId>
            <artifactId>h2</artifactId>
            <scope>runtime</scope>
        </dependency>
```

- Spring Batch :
```xml
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-batch</artifactId>
        </dependency>
```

- Lombok :
```xml
		<dependency>
			<groupId>org.projectlombok</groupId>
			<artifactId>lombok</artifactId>
		</dependency>
```

- Infrastructure and Batch Core :
```xml
		<dependency>
			<groupId>org.springframework.batch</groupId>
			<artifactId>spring-batch-core</artifactId>
			<version>5.1.1</version>
		</dependency>
		<dependency>
			<groupId>org.springframework.batch</groupId>
			<artifactId>spring-batch-infrastructure</artifactId>
			<version>5.1.1</version>
		</dependency>
```

### Steps to configure spring batch example 1 :

#### Step 1

**+ Create Person entity :**

> Person.java

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person {
    String firstName;
    String lastName;
    Integer age;
    Boolean active;
}
```

#### Step 2

**+ Create batch configuration and schema for database :**
  
> BatchConfig.java

```java
@Configuration
@EnableBatchProcessing
public class BatchConfig {
    DataSource dataSource;
    public BatchConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    @Bean
    public DataSourceTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }
    @Bean
    public DataSourceInitializer databasePopulator() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("org/springframework/batch/core/schema-h2.sql"));
        populator.addScript(new ClassPathResource("sql/batch-schema.sql"));
        populator.setContinueOnError(false);
        populator.setIgnoreFailedDrops(false);
        DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
        dataSourceInitializer.setDataSource(dataSource);
        dataSourceInitializer.setDatabasePopulator(populator);
        return dataSourceInitializer;
    }
}
```

> batch-schema.sql

```sql
DROP TABLE IF EXISTS person;
CREATE TABLE person  (
    person_id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    age INTEGER,
    is_active BOOLEAN
);
```

#### Step 3

**+ Create CSV file :**
  
> person.csv

```csv
Blokish,Gupta,41,true
Brian,Schultz,42,false
John,Cena,43,true
Albert,Pinto,44,false
```

#### Step 4

**+ Create CSV to database Job for read CSV file :**
  
> CsvToDatabaseJob.java

```java
public class CsvToDatabaseJob {}
```

**+ Add Job and Step :**

```java
    @Bean
    public Job csvToDatabaseJob() {
        return jobBuilderFactory.get("csvToDatabaseJob")
                .start(step1())
                .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<Person, Person>chunk(10)
                .reader(reader())
                .processor(personItemProcessor)
                .writer(writer())
                .build();
    }
```

**+ CSV Reading :**

```java
    @Bean
    public FlatFileItemReader<Person> reader() {
        return new FlatFileItemReaderBuilder<Person>()
                .name("personItemReader")
                .resource(new ClassPathResource("data.csv"))
                .delimited()
                .names(new String[]{"firstName", "lastName", "age", "active"})
                .fieldSetMapper(new BeanWrapperFieldSetMapper<Person>() {{
                    setTargetType(Person.class);
                }})
                .build();
    }

    @Bean
    public ItemWriter<Person> writer() {
        return new CompositeItemWriterBuilder<Person>()
                .delegates(new JdbcBatchItemWriterBuilder<Person>()
                        .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                        .sql("INSERT INTO person (first_name, last_name, age, active) VALUES (:firstName, :lastName, :age, :active)")
                        .dataSource(dataSource)
                        .build())
                .build();
    }
```
