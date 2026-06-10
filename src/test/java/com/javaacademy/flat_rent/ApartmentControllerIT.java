package com.javaacademy.flat_rent;

import com.javaacademy.flat_rent.dto.ApartmentRqDto;
import com.javaacademy.flat_rent.dto.ApartmentRsDto;
import com.javaacademy.flat_rent.entity.Apartment;
import com.javaacademy.flat_rent.entity.RoomCount;
import com.javaacademy.flat_rent.repository.ApartmentRepository;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApartmentControllerIT {
    @Autowired
    ApartmentRepository apartmentRepository;

    @LocalServerPort
    private int port;
    private RequestSpecification reqSpec;

    @BeforeEach
    void setUp() {
        reqSpec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .setPort(port)
                .build();
    }

    private final ResponseSpecification resSpec = new ResponseSpecBuilder()
            .expectContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    @Test
    @DisplayName("Создание квартиры")
    @Sql(value = "/clearApartmentOnCascade.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void expectCreateApartment() {

        ApartmentRqDto body = new ApartmentRqDto(
                null,
                "Omsk",
                "Popova",
                "1",
                "9",
                RoomCount.ROOM);

        ApartmentRsDto response = RestAssured
                .given()
                .spec(reqSpec)
                .body(body)
                .post("/apartment")
                .then()
                .spec(resSpec)
                .statusCode(201)
                .body("id", notNullValue())
                .body("city", is("Omsk"))
                .body("street", is("Popova"))
                .body("house", is("1"))
                .body("corpus", is("9"))
                .body("apartment_type", is("ROOM"))
                .extract().as(ApartmentRsDto.class);

        Apartment apartment = apartmentRepository.findById(response.id())
                .orElseThrow(EntityNotFoundException::new);
        Assertions.assertEquals("Omsk", apartment.getCity());
        Assertions.assertEquals("9", apartment.getCorpus());
        Assertions.assertEquals(RoomCount.ROOM, apartment.getRoomCount());
        assertThat(apartment.getId()).isNotNull();
        assertThat(apartment.getCity()).isEqualTo("Omsk");
        assertThat(apartment.getStreet()).isEqualTo("Popova");
        assertThat(apartment.getHouse()).isEqualTo("1");
        assertThat(apartment.getCorpus()).isEqualTo("9");
        assertThat(apartment.getRoomCount()).isEqualTo(RoomCount.ROOM);
    }
}
