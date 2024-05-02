package com.example.springBatchExample;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person {
    String firstName;
    String lastName;
    Integer age;
    Boolean active;
}
