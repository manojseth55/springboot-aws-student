package com.student.studentwrkr.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class StudentWrkrProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {

        String request = exchange.getIn().getBody(String.class);

        log.info("event=studentWrkrProcessor, status=processStart, data={}", request);

    }

}
