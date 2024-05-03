package com.example.springBatchExample.step;

import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class Writer implements ItemWriter<String> {

    @Override
    public void write(List<? extends String> message) throws Exception {
        for (String msg : message) {
            System.out.println("Writing the data " + message);
        }
    }
}
