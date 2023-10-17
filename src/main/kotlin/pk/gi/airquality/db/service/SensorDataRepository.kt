package pk.gi.airquality.db.service

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Service
import pk.gi.airquality.db.model.SensorData
import java.time.LocalDateTime

@Service
interface SensorDataRepository : CrudRepository<SensorData, Long> {
    fun deleteAllByValueNull()

    fun existsByDateAndSensorId(date: LocalDateTime, sensorId: Long): Boolean
}