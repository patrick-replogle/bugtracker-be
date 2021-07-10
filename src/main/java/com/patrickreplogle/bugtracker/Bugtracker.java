package com.patrickreplogle.bugtracker;

import com.patrickreplogle.bugtracker.util.Constants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class Bugtracker {

    private static boolean stop = false;

    private static void checkEnvironmentVariable(String envvar) {
        if (System.getenv(envvar) == null) {
            System.out.println("Missing " + envvar + " environment variable.");
            stop = true;
        }
    }

    public static void main(String[] args) {
        checkEnvironmentVariable(Constants.OAUTHCLIENTID);
        checkEnvironmentVariable(Constants.OAUTHCLIENTSECRET);

        if (!stop) {
            SpringApplication.run(Bugtracker.class, args);
        }
    }
}
