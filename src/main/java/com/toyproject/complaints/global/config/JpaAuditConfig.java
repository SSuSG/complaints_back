package com.toyproject.complaints.global.config;

import com.toyproject.complaints.domain.user.entity.User;
import com.toyproject.complaints.global.util.AuditAwareImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
public class JpaAuditConfig {

    @Bean
    public AuditorAware<String> auditorProvider(){
        return new AuditAwareImpl();
    }
}
