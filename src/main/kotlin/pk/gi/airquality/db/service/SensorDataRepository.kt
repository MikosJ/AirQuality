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
                "    AVG(sd.value) AS averageValue,\n" +
                "    sd.parameter_formula AS parameterFormula,\n" +
                "    sd.parameter_name AS parameterName,\n" +
                "    c.province_name AS voivodeship\n" +
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
                "GROUP BY\n" +
                "    c.province_name , sd.parameter_formula;\n", nativeQuery = true
    )
    fun findAverageValueForParameter(interval: Number): List<Tuple>


//    @Query(
//                value = "SELECT\n" +
//                "    AVG(sd.value) AS averageValue,\n" +
//                "    sd.parameter_formula AS parameterFormula,\n" +
//                "    sd.parameter_name AS parameterName,\n" +
//                "    st.station_name AS stationName,\n" +
//                "    c.commune_name AS city,\n" +
//                "    c.province_name AS voivodeship,\n" +
//                "    st.gegr_lon AS longitude,\n" +
//                "    st.gegr_lat AS latitude,\n" +
//                "    st.station_id AS stationId\n" +
//                "FROM\n" +
//                "    sensor_data sd\n" +
//                "        JOIN\n" +
//                "    sensor s ON sd.sensor_id = s.id\n" +
//                "        JOIN\n" +
//                "    station st ON s.station_station_id = st.station_id\n" +
//                "        JOIN\n" +
//                "    city c ON st.city_id = c.id\n" +
//                "WHERE\n" +
//                "        sd.date >= NOW() - INTERVAL :interval HOUR\n" +
//                "GROUP BY\n" +
//                "    st.station_name , sd.parameter_formula;\n", nativeQuery = true)
//    fun findAverageValueForStation(interval: Number, stationId: Number): List<Tuple>


    @Query(
        "SELECT\n" +
                "sd.value,\n" +
                "sd.date,\n" +
                "trim(sd.parameter_formula) AS parameterFormula,\n" +
                "trim(sd.parameter_name) AS parameterName,\n" +
                "trim(st.station_name) AS stationName,\n" +
                "trim(c.commune_name) AS city,\n" +
                "trim(c.province_name) AS voivodeship,\n" +
                "st.gegr_lon AS longitude,\n" +
                "st.gegr_lat AS latitude,\n" +
                "st.station_id AS stationId\n" +
                "FROM\n" +
                "    sensor_data sd\n" +
                "        JOIN\n" +
                "    sensor s ON sd.sensor_id = s.id\n" +
                "        JOIN\n" +
                "    station st ON s.station_station_id = st.station_id\n" +
                "        JOIN\n" +
                "    city c ON st.city_id = c.id\n" +
                "WHERE\n" +
                "        sd.date >= NOW() - INTERVAL :interval HOUR AND station_id = :stationId\n" +
                "group by sd.parameter_formula, station_name, date", nativeQuery = true
    )
    fun findValuesForStationAndInterval(stationId: Number, interval: Number): List<Tuple>

    @Query(
        "SELECT\n" +
                "    AVG(sd.value) as averageValue,\n" +
                "    sd.date,\n" +
                "    trim(sd.parameter_formula) AS parameterFormula,\n" +
                "    trim(sd.parameter_name) AS parameterName,\n" +
                "    trim(c.province_name) AS voivodeship\n" +
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
                "group by sd.parameter_formula, date, voivodeship", nativeQuery = true
    )
    fun findAverageVoivodeshipData(interval: Number): List<Tuple>
}
