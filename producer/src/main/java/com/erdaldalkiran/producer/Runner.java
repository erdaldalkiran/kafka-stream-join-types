package com.erdaldalkiran.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class Runner implements CommandLineRunner {
    private final Environment env;
    private final ApplicationContext context;

    @Override
    public void run(String... args) throws Exception {
        var type = env.getProperty("type", Type.class);
        var producer = (IProducer)context.getBean(type.getProducer());
        producer.run();

    }
}
