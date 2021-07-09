package com.patrickreplogle.bugtracker.services;

import org.springframework.data.domain.AuditorAware;

import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserAuditing implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of("SYSTEM");
    }
}
