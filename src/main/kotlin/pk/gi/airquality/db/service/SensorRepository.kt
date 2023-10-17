package pk.gi.airquality.db.service

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Service
import pk.gi.airquality.db.model.Sensor

@Service
interface SensorRepository : CrudRepository<Sensor, Long>
