package az.company.qwisedemoapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class QwiseDemoAppApplication {

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Baku"));
        SpringApplication.run(QwiseDemoAppApplication.class, args);
    }
}
