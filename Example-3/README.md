# Spring Batch example 3

### Prerequisites to start spring batch example 3 :

> pom.xml

- Spring Batch (starter, core and infrastructure) :

```xml
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-batch</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.batch</groupId>
            <artifactId>spring-batch-core</artifactId>
            <version>4.3.7</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.batch</groupId>
            <artifactId>spring-batch-infrastructure</artifactId>
            <version>4.3.7</version>
        </dependency>
```

- MySQL Database :

```xml
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>8.0.33</version>
        </dependency>
```

- opencsv :
  
```xml
        <dependency>
            <groupId>com.opencsv</groupId>
            <artifactId>opencsv</artifactId>
            <version>4.1</version>
            <scope>test</scope>
        </dependency>
```
  
- javafaker :

```xml
        <dependency>
            <groupId>com.github.javafaker</groupId>
            <artifactId>javafaker</artifactId>
            <version>0.16</version>
            <scope>test</scope>
        </dependency>
```

- CSV file (path: Example-3/src/main/resources/csv/student.csv) :

```csv
id,firstname,lastname,age,gender,dateOfBirthday,email,score,status
1,John,Doe,25,Male,1999-05-10,johndoe@example.com,85.5,Active
2,Jane,Smith,23,Female,2001-08-15,janesmith@example.com,78.2,Active
3,Michael,Johnson,27,Male,1997-12-20,michaeljohnson@example.com,91.3,Active
4,Emily,Brown,24,Female,1998-10-05,emilybrown@example.com,79.9,Active
5,William,Miller,26,Male,1996-07-30,williammiller@example.com,88.7,Active
```

> application.yml

```yml
sourcepath: student.csv
source-output: src/main/resources/csv/student.csv

spring:
  batch:
    jdbc:
      initialize-schema: always
  datasource:
    url: jdbc:mysql://localhost:3306/student
    username: root
    password:
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    database-platform: org.hibernate.dialect.MySQLDialect
    hibernate:
      ddl-auto: update
    show-sql: true
  ```

### Demo of spring batch example 3 :

- Output : 

```bash
INFO 19552 --- [           main] c.e.s.SpringBatchExampleApplication      : Started SpringBatchExampleApplication in 2.052 seconds (JVM running for 2.585)
INFO 19552 --- [           main] o.s.b.a.b.JobLauncherApplicationRunner   : Running default command line with: []
INFO 19552 --- [           main] o.s.b.c.l.support.SimpleJobLauncher      : Job: [FlowJob: [name=importUserJob]] launched with the following parameters: [{run.id=1}]
INFO 19552 --- [           main] o.s.batch.core.job.SimpleStepHandler     : Executing step: [step]
INFO 19552 --- [           main] o.s.batch.core.step.AbstractStep         : Step: [step] executed in 51ms
INFO 19552 --- [           main] o.s.b.c.l.support.SimpleJobLauncher      : Job: [FlowJob: [name=importUserJob]] completed with the following parameters: [{run.id=1}] and the following status: [COMPLETED] in 84ms
```

- Database :

![image](https://github.com/BadrOuaddah/Spring-Batch-example/assets/119801735/ba92decf-4f25-424e-a99f-2b8dead287c1)
