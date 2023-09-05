package pk.gi.airquality.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
class ApplicationConfig {
    val restTemplate = RestTemplate()

    @Bean
    fun restTemplate(): RestTemplate {
        return restTemplate
    }
}