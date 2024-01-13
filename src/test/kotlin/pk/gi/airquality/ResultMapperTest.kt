import com.fasterxml.jackson.annotation.JsonFormat
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import jakarta.persistence.Tuple
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import pk.gi.airquality.mapper.ResultMapper
import pk.gi.airquality.model.rest.out.Parameter
import pk.gi.airquality.model.rest.out.Result
import java.math.BigDecimal
import java.sql.Timestamp
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit
import kotlin.reflect.typeOf


@ExperimentalCoroutinesApi
class ResultMapperTest {

    private lateinit var resultMapper: ResultMapper

    @BeforeEach
    fun setUp() {
        resultMapper = ResultMapper()
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `mapEachTupleToDataResult should map tuples to Result objects`(): Unit = runBlocking {
        // Arrange
        val tuple1 = mockk<Tuple> {
            coEvery { get(0, BigDecimal::class.java) } returns BigDecimal.ONE
            coEvery { get(1, Timestamp::class.java) } returns Timestamp(
                LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)*1000
            )
            coEvery { get(2, String::class.java) } returns "PM2.5"
            coEvery { get(3, String::class.java) } returns "pył zawieszony PM2.5"
            coEvery { get(4, String::class.java) } returns "ul. Dietla"
            coEvery { get(5, String::class.java) } returns "Kraków"
            coEvery { get(6, String::class.java) } returns "Małopolska"
            coEvery { get(7, Number::class.java) } returns 44.44
            coEvery { get(8, Number::class.java) } returns 19.19
            coEvery { get(9, Number::class.java) } returns 1
        }
        val tuple2 = mockk<Tuple> {
            coEvery { get(0, BigDecimal::class.java) } returns BigDecimal.ONE
            coEvery { get(1, Timestamp::class.java) } returns Timestamp(
                LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)*1000
            )
            coEvery { get(2, String::class.java) } returns "PM10"
            coEvery { get(3, String::class.java) } returns "pył zawieszony PM10"
            coEvery { get(4, String::class.java) } returns "ul. Dekerta"
            coEvery { get(5, String::class.java) } returns "Kraków"
            coEvery { get(6, String::class.java) } returns "Małopolska"
            coEvery { get(7, Number::class.java) } returns 44.56
            coEvery { get(8, Number::class.java) } returns 19.26
            coEvery { get(9, Number::class.java) } returns 1
        }
        val tupleList = listOf(tuple1, tuple2)

        // Act
        val result = resultMapper.mapEachTupleToDataResult(tupleList)

        // Assert
        assertEquals(2, result.size)
        assertEquals(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusHours(1), result[0].date)
        assertEquals("Kraków", result[0].city)
        assertEquals(BigDecimal.ONE, result[0].value)
        assertThat(result[0]).isInstanceOf(Result::class.java)
    }

    @Test
    fun `mapSensorDataToParameters should map sensor data to parameters`() {
        // Arrange
        val result1 = Result(
            BigDecimal.ONE,
            LocalDateTime.now(),
            "PM2.5",
            "pył zawieszony PM2.5",
            "ul. Polna 7",
            "Mińsk Mazowiecki",
            "Mazowieckie",
            53.21,
            25.25,
            1
        )

        val result2 = Result(
            BigDecimal.ONE,
            LocalDateTime.now(),
            "PM10",
            "pył zawieszony PM10",
            "ul. Dietla",
            "Kraków",
            "Małopolskie",
            45.44,
            18.12,
            1
        )

        val result3 = Result(
            BigDecimal.ONE,
            LocalDateTime.now(),
            "CO",
            "Tlenek węgla",
            "ul. Jasna",
            "Oświęcim",
            "Małopolska",
            42.44,
            17.12,
            1
        )

        val stationSensorData = listOf(result1, result2, result3)
        val isParameterSpecified = false
        val parameterFormula: String? = null

        // Act
        val parameters =
            ResultMapper().mapSensorDataToParameters(stationSensorData, isParameterSpecified, parameterFormula)

        // Assert
        assertEquals(3, parameters.size)
        assertEquals("pył zawieszony PM2.5", parameters[0].name)
        assertEquals("PM2.5", parameters[0].formula)
        assertEquals(1, parameters[0].values.size)
        assertEquals("pył zawieszony PM10", parameters[1].name)
        assertEquals("PM10", parameters[1].formula)
        assertEquals(1, parameters[1].values.size)
        assertThat(parameters[0]).isInstanceOf(Parameter::class.java)
    }
}


