package com.example.springBatchExample.step;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

public class Writer implements ItemWriter<String> {
    @Override
    public void write(Chunk<? extends String> chunk) throws Exception {
        for (String chnk : chunk) {
            System.out.println("Writing the data " + chnk);
        }
    }
}
