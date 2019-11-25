package de.predic8.GraalRDemo;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Source;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.function.Function;

@Controller
@SpringBootApplication
public class GraalRDemoApplication {

    @Value(value = "classpath:plot.R")
    private Resource rSource;

    @Autowired
    private Function<Double, String> plotFunction;

    @Bean
    Function getPlotFunction(@Autowired Context ctx) throws IOException {
        return ctx.eval(Source.newBuilder("R", rSource.getURL()).build()).as(Function.class);
    }

    @RequestMapping(value = "/polyglot", produces = "image/svg+xml")
    public ResponseEntity<String> load() {
        return new ResponseEntity<>(plotFunction.apply(1d),
                HttpStatus.OK);
    }

    public static void main(String[] args) {
        SpringApplication.run(GraalRDemoApplication.class, args);
    }

    @Bean
    public Context getGraalVMContext() {
        return Context.newBuilder().allowAllAccess(true).build();
    }
}
