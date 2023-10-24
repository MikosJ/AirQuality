package pk.gi.airquality

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.reactive.config.EnableWebFlux

@SpringBootApplication
@EnableWebFlux
class AirQualityApplication

fun main(args: Array<String>) {
	runApplication<AirQualityApplication>()
}
