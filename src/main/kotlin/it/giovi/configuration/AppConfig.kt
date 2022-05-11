package it.giovi.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing


@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
class AppConfig {

    @Bean
    fun auditorAware(): AuditorAware<String> {
        return CustomAuditorAwareImpl()
    }
}