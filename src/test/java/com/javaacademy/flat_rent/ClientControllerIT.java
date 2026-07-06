package com.javaacademy.flat_rent;

import com.javaacademy.flat_rent.repository.BookingRepository;
import com.javaacademy.flat_rent.repository.ClientRepository;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ClientControllerIT {
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    BookingRepository bookingRepository;

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
    @DisplayName("Удаление клиента по id")
    @Sql(value = "/createClient.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/clearClient.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void deleteClientById() {
        assertTrue(clientRepository.existsById(1L));
        assertThat(clientRepository.existsById(1L)).isTrue();

        RestAssured
                .given()
                .spec(reqSpec)
                .delete("/client/1")
                .then()
                .spec(resSpec)
                .statusCode(204);

        assertTrue(clientRepository.findById(1L).isEmpty());
        assertThat(clientRepository.findById(1L)).isEmpty();
    }

    @Test
    @DisplayName("Удаление связанных броней при удалении клиента")
    @Sql(value = {"/clearBookingAdvertApartmentClient.sql", "/createClientAdvertApartmentBooking.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void deletedClientShouldDeletedHisBooking() {
        assertThat(clientRepository.count()).isEqualTo(3);
        // добавил зависимость bookingRepository
        assertThat(bookingRepository.count()).isEqualTo(3);
        assertThat(clientRepository.existsById(1L)).isTrue();
        assertThat(bookingRepository.existsById(1L)).isNotNull();

        RestAssured
                .given()
                .spec(reqSpec)
                .delete("/client/{id}", 1L)
                .then()
                .spec(resSpec)
                .statusCode(204);

        assertThat(clientRepository.count()).isEqualTo(2);
        assertThat(bookingRepository.count()).isEqualTo(2);
        assertThat(clientRepository.existsById(1L)).isFalse();
        assertThat(bookingRepository.existsById(1L)).isFalse();
    }
}
