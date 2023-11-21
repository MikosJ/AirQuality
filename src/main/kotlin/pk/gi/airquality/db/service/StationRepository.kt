package pk.gi.airquality.db.service

import jakarta.persistence.Tuple
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import pk.gi.airquality.db.model.Station

interface StationRepository: CrudRepository<Station, Long> {
    @Query("SELECT station_name, station_id, c.name from station join jakub_mikos.city c on c.id = station.city_id", nativeQuery = true)
    fun findAllStations(): List<Tuple>
}
