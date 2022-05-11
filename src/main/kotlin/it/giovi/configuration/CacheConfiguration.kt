package it.giovi.configuration

import org.springframework.cache.annotation.CachingConfigurerSupport

import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.cache.interceptor.KeyGenerator

@Configuration
@EnableCaching
class CacheConfiguration : CachingConfigurerSupport() {
    @Bean("customKeyGenerator")
    override fun keyGenerator(): KeyGenerator? {
        return CustomKeyGenerator()
    }
}
