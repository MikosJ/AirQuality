package pk.gi.airquality.db.service

import jakarta.persistence.Tuple
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Service
import pk.gi.airquality.db.model.SensorData
import java.time.LocalDateTime

@Service
interface SensorDataRepository : CrudRepository<SensorData, Long> {

    fun existsByDateAndSensorId(date: LocalDateTime, sensorId: Long): Boolean


    @Query(
        value = "SELECT\n" +
                "    sd.value AS value,\n" +
                "    sd.date AS date,\n" +
                "    sd.parameter_formula AS parameterFormula,\n" +
                "    sd.parameter_name AS parameterName,\n" +
                "    st.station_name AS stationName,\n" +
                "    c.commune_name AS city,\n" +
                "    c.province_name AS voivodeship,\n" +
                "    st.gegr_lon AS longitude,\n" +
                "    st.gegr_lat AS latitude,\n" +
                "    st.station_id AS stationId\n" +
                "FROM\n" +
                "    sensor_data sd\n" +
                "        JOIN\n" +
                "    sensor s ON sd.sensor_id = s.id\n" +
                "        JOIN\n" +
                "    station st ON s.station_station_id = st.station_id\n" +
                "        JOIN\n" +
                "    city c ON st.city_id = c.id\n" +
                "WHERE\n" +
                "        sd.date >= NOW() - INTERVAL :interval HOUR;\n", nativeQuery = true
    )
    fun findAllDataWithStationNameAndParamName(interval: Number): List<Tuple>

    @Query(
        value = "SELECT\n" +
                "AVG(sd.value) AS averageValue,\n" +
                "sd.parameter_formula AS parameterFormula,\n" +
                "sd.parameter_name AS parameterName,\n" +
                "st.station_name AS stationName,\n" +
                "c.province_name AS voivodeship,\n" +
                "st.gegr_lat AS latitude,\n" +
                "st.gegr_lon AS longitude\n" +
                "FROM\n" +
                "    sensor_data sd\n" +
                "        JOIN\n" +
                "    sensor s ON sd.sensor_id = s.id\n" +
                "        JOIN\n" +
                "    station st ON s.station_station_id = st.station_id\n" +
                "        JOIN\n" +
                "    city c ON st.city_id = c.id\n" +
                "WHERE\n" +
                "        sd.date >= NOW() - INTERVAL :interval HOUR\n" +
                "group by c.commune_name, sd.parameter_formula,station_name\n" +
                "    order by TRIM(station_name)", nativeQuery = true
    )
    fun findAverageValueForParameter(interval: Number): List<Tuple>

}
