package pk.gi.airquality.db.service

import org.springframework.data.repository.CrudRepository
import pk.gi.airquality.db.model.Station

interface StationRepository: CrudRepository<Station, Long>