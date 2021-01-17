package io.github.hylexus.jt808.samples.annotation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

/**
 * @author hylexus
 * Created At 2020-01-28 8:47 下午
 */
@SpringBootApplication
//@EnableJt808ServerAutoConfig
@ComponentScan({"com.dudu","io.github.hylexus.jt808"})
public class Jt808ServerSampleAnnotationApplication {

    public static void main(String[] args) {
        SpringApplication.run(Jt808ServerSampleAnnotationApplication.class, args);
    }

}
