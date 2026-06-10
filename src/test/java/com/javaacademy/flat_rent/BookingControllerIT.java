package com.javaacademy.flat_rent;

import com.javaacademy.flat_rent.dto.BookingRqDto;
import com.javaacademy.flat_rent.dto.ClientRqDto;
import com.javaacademy.flat_rent.entity.Booking;
import com.javaacademy.flat_rent.entity.RoomCount;
import com.javaacademy.flat_rent.repository.BookingRepository;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookingControllerIT {
    @Autowired
    private BookingRepository bookingRepository;
    @LocalServerPort
    private int port;
    private RequestSpecification reqSpec;
    private final ResponseSpecification resSpec = new ResponseSpecBuilder()
            .log(LogDetail.ALL)
            .build();

    @BeforeEach
    void setUp() {
        reqSpec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .setPort(port)
                .build();
    }

    @Test
    @DisplayName("Успешное создание бронирования")
    @Sql(value = "/clearBookingAdvertApartmentClient.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/createAdvertAndApartment.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    //@Sql(value = "/createClient.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void expectCreateBookingSuccess() {
        assertThat(bookingRepository.findAll()).isEmpty();

        ClientRqDto client = new ClientRqDto(
                null,
                "Oleg",
                "test@mail.com");

        BookingRqDto body = new BookingRqDto(
                null,
                LocalDate.now(),
                LocalDate.now().plusDays(1),
                client,
                1L);

        RestAssured
                .given()
                .spec(reqSpec)
                .body(body)
                .post("/booking")
                .then()
                .spec(resSpec)
                .statusCode(201);

        List<Booking> bookings = bookingRepository.findAll();
        assertThat(bookings.size()).isEqualTo(1);
        Booking booking = bookings.stream().findFirst().orElseThrow();
        assertThat(booking.getId()).isNotNull();
        assertThat(booking.getStartDate()).isToday();
        assertThat(booking.getEndDate()).isEqualTo(body.endDate());
        assertThat(booking.getTotalPrice()).isEqualTo(BigDecimal.valueOf(ChronoUnit.DAYS.between(
                        body.startDate(),
                        body.endDate())).multiply(
                        booking.getAdvert().getPrice()));

        assertThat(booking.getClient().getId()).isNotNull();
        assertThat(booking.getClient().getName()).isEqualTo("Oleg");
        assertThat(booking.getClient().getEmail()).isEqualTo("test@mail.com");

        assertThat(booking.getAdvert().getId()).isEqualTo(1);
        assertThat(booking.getAdvert().getPrice()).isEqualByComparingTo(BigDecimal.TEN);
        assertThat(booking.getAdvert().getIsActive()).isTrue();
        assertThat(booking.getAdvert().getDescription()).isEqualTo("Один");

        assertThat(booking.getApartment().getId()).isEqualTo(1);
        assertThat(booking.getApartment().getCity()).isEqualTo("Tula");
        assertThat(booking.getApartment().getStreet()).isEqualTo("Java");
        assertThat(booking.getApartment().getHouse()).isEqualTo("7");
        assertThat(booking.getApartment().getCorpus()).isEqualTo("1A");
        assertThat(booking.getApartment().getRoomCount()).isEqualTo(RoomCount.ONE);
    }

    @Test
    @DisplayName("Получение всех броней по email")
    @Sql(value = "/clearBookingAdvertApartmentClient.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/createClientAdvertApartmentBooking.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    public void getAllBookingsByEmailSuccess() {
        RestAssured
                .given()
                .spec(reqSpec)
                //.queryParam("email", "test1@mail.ru")
                .get("/booking/by-email?email=test1@mail.ru")
                .then()
                .spec(resSpec)
                .contentType(ContentType.JSON)
                .statusCode(200)
                .body("content.size()", is(1))
                .body("content[0].id", is(1))
                .body("content[0].date_start", is("2026-06-10"))
                .body("content[0].date_finish", is("2026-06-11"))
                .body("content[0].client.id", is(1))
                .body("content[0].client.name", is("Oleg"))
                .body("content[0].client.email", is("test1@mail.ru"))
                .body("content[0].advert.id", is(1))
                .body("content[0].advert.price", is(10f))
                .body("content[0].advert.is_active", is(true))
                // обычно проверяют обязательные поля, без глубокой вложенности (id email price total_price) и
                // бизнес-значимые поля client.email advert.id
                .body("content[0].advert.apartment.id", is(1))
                .body("content[0].advert.apartment.city", is("Tula"))
                .body("content[0].advert.apartment.street", is("Java"))
                .body("content[0].advert.apartment.house", is("7"))
                .body("content[0].advert.apartment.corpus", is("1A"))
                .body("content[0].advert.apartment.apartment_type", is("ONE"))
                .body("content[0].advert.description", is("Один"))
                .body("content[0].result_price", is(100f))
                .body("first", is(true))
                .body("last", is(true))
                .body("pageable.pageSize", is(20))
                .body("pageable.sort.sorted", is(true))
                .body("totalElements", is(1))
                .body("totalPages", is(1));
    }
}
