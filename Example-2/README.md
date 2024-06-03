# Spring Batch example 2

### Prerequisites to start spring batch example 2 :

- H2 Database :

```xml
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

### Steps to configure spring batch example 2 :

> Step 1

Create Reader, Processor and Writer classes into package named step:

**+ Reader implements ItemReader :**

```java
public class Reader implements ItemReader<String> {

    private String[] messages = {"javainuse.com",
            "Welcome to Spring Batch Example",
            "We use H2 Database for this example"};

    private int count = 0;

    @Override
    public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {

        if (count < messages.length) {
            return messages[count++];
        } else {
            count = 0;
        }

        return null;
    }
}
```

**+ Processor implements ItemProcessor :**

```java
public class Processor implements ItemProcessor<String, String> {
    @Override
    public String process(String data) throws Exception {
        return data.toUpperCase();
    }
}
```

**+ Writer implements ItemWriter :**

```java
public class Writer implements ItemWriter<String> {

    @Override
    public void write(List<? extends String> message) throws Exception {
        for (String msg : message) {
            System.out.println("Writing the data " + message);
        }
    }
}
```

> Step 2

**+ Create BatchConfig :**

```java
@Configuration
public class BatchConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    public BatchConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    public Job processJob() {
        return jobBuilderFactory.get("processJob")
                .incrementer(new RunIdIncrementer()).listener(listener())
                .flow(orderStep1()).end().build();
    }

    @Bean
    public Step orderStep1() {
        return stepBuilderFactory.get("orderStep1").<String, String> chunk(1)
                .reader(new Reader()).processor(new Processor())
                .writer(new Writer()).build();
    }
}

```

> Step 3

**+ Create JobCompletionListener extends JobExecutionListenerSupport :**

```java
public class JobCompletionListener extends JobExecutionListenerSupport {

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            System.out.println("BATCH STATUS: BATCH JOB COMPLETED SUCCESSFULLY !");
        }
    }
}
```

**+ Import JobCompletionListener to BatchConfig class :**

```java
    @Bean
    public JobExecutionListener listener() {
        return new JobCompletionListener();
    }
```

### Run and result of spring batch example 2 :

**After run spring boot application and see the result :**

```bash
INFO 20980 --- [  restartedMain] o.s.b.a.b.JobLauncherApplicationRunner   : Running default command line with: []
INFO 20980 --- [  restartedMain] o.s.b.c.l.support.SimpleJobLauncher      : Job: [FlowJob: [name=processJob]] launched with the following parameters: [{run.id=1}]
INFO 20980 --- [  restartedMain] o.s.batch.core.job.SimpleStepHandler     : Executing step: [orderStep1]
Writing the data [JAVAINUSE.COM]
Writing the data [WELCOME TO SPRING BATCH EXAMPLE]
Writing the data [WE USE H2 DATABASE FOR THIS EXAMPLE]
INFO 20980 --- [  restartedMain] o.s.batch.core.step.AbstractStep         : Step: [orderStep1] executed in 19ms
BATCH STATUS: BATCH JOB COMPLETED SUCCESSFULLY !
INFO 20980 --- [  restartedMain] o.s.b.c.l.support.SimpleJobLauncher      : Job: [FlowJob: [name=processJob]] completed with the following parameters: [{run.id=1}] and the following status: [COMPLETED] in 33ms
```
