package pk.gi.airquality.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.web.client.RestTemplate
@Configuration
@EnableAsync
@EnableScheduling
class ApplicationConfig {
    val restTemplate = RestTemplate()

    @Bean
    fun restTemplate(): RestTemplate {
        return restTemplate
    }


}
