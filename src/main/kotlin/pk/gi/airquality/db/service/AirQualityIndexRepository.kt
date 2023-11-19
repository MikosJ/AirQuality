package pk.gi.airquality.db.service

import jakarta.persistence.Tuple
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import pk.gi.airquality.db.model.AirQualityIndex
import pk.gi.airquality.db.model.Station
import java.time.LocalDateTime

interface AirQualityIndexRepository: CrudRepository<AirQualityIndex, Long> {

    @Query("SELECT  station_id_station_id, index_level_id, index_level_name, st_calc_date\n" +
            "FROM air_quality_index\n" +
            "where st_calc_date >= NOW() - INTERVAL :interval HOUR\n" +
            "order by st_calc_date desc", nativeQuery = true)
    fun findAllByStCalcDateAfter(interval: Number): List<Tuple>

    fun findAllByStCalcDateAndStationId(stCalcDate: LocalDateTime, stationId: Station): List<AirQualityIndex>
}