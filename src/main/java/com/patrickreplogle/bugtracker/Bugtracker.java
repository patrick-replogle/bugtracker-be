package com.patrickreplogle.bugtracker;

import com.patrickreplogle.bugtracker.util.Constants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class Bugtracker {

    private static boolean stop = false;

    private static void checkEnvironmentVariables(String[] envVarsList) {
        for (String envvar : envVarsList) {
            if (System.getenv(envvar) == null) {
                System.out.println("Missing " + envvar + " environment variable.");
                stop = true;
            }
        }
    }

    public static void main(String[] args) {
        checkEnvironmentVariables(Constants.requiredEnvVars);

        if (!stop) {
            SpringApplication.run(Bugtracker.class, args);
        }
    }
}
