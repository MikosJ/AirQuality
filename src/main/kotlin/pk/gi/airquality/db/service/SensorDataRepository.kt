package pk.gi.airquality.db.service

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Service
import pk.gi.airquality.db.model.SensorData
import pk.gi.airquality.service.DataProviderService
import java.time.LocalDateTime

@Service
interface SensorDataRepository : CrudRepository<SensorData, Long> {

    fun existsByDateAndSensorId(date: LocalDateTime, sensorId: Long): Boolean

    fun findAllBySensorStationStationIdAndDateAfter(stationID: Long, date: LocalDateTime): List<SensorData>

    @Query(
        value = "SELECT\n" +
                "    s.station_name,\n" +
                "    c.commune_name AS station_city,\n" +
                "    sd.parameter_formula,\n" +
                "    sd.parameter_name,\n" +
                "    sd.value,\n" +
                "    sd.date\n" +
                "FROM\n" +
                "    sensor_data sd\n" +
                "        JOIN\n" +
                "        sensor ON sd.sensor_id = sensor.id\n" +
                "        JOIN\n" +
                "    station s ON station_id = sensor.station_station_id\n" +
                "        JOIN\n" +
                "    city c ON s.city_id = c.id\n" +
                "WHERE\n" +
                "        sd.date >= NOW() - INTERVAL 3 HOUR\n" +
                ";\n", nativeQuery = true
    )
    fun findAllDataWithStationNameAndParamName(): List<DataProviderService.ResultProjection>
}
