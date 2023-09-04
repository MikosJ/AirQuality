package pk.gi.airquality

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AirQualityApplication

fun main(args: Array<String>) {
	runApplication<AirQualityApplication>(*args)
}
