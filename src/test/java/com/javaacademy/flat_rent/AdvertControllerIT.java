package com.javaacademy.flat_rent;

import com.javaacademy.flat_rent.dto.AdvertRqDto;
import com.javaacademy.flat_rent.entity.Advert;
import com.javaacademy.flat_rent.repository.AdvertRepository;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class AdvertControllerIT {
    @Autowired
    AdvertRepository advertRepository;

    private final RequestSpecification reqSpec = new RequestSpecBuilder()
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    private final ResponseSpecification resSpec = new ResponseSpecBuilder()
            .expectContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    @Test
    @DisplayName("Создание объявления")
    @Sql(value = "/createApartment.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/clearApartmentOnCascade.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void createAdvert() {
        AdvertRqDto body = new AdvertRqDto(
                null,
                BigDecimal.valueOf(150),
                true,
                1L,
                "Просторная однокомнатная квартира в центре");

        RestAssured
                .given()
                .spec(reqSpec)
                .body(body)
                .post("/advert")
                .then()
                .spec(resSpec)
                .statusCode(201)
                .body("price", is(150))
                .body("apartment.id", is(1))
                .body("apartment.street", is("Java"))
                .body("apartment.corpus", is("1"))
                .body("description", is("Просторная однокомнатная квартира в центре"));

        Optional<Advert> advert = advertRepository.findAll().stream().findFirst();
        Assertions.assertEquals(0, BigDecimal.valueOf(150).compareTo(advert.get().getPrice()));
    }

    @Test
    @DisplayName("Получить все объявления без названия города (result - Moscow)")
    @Sql(value = "/createAdvertAndApartment.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/clearApartmentOnCascade.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAllAdvertsByNameCity() {
        RestAssured
                .given()
                .spec(reqSpec)
                .get("/advert")
                .then()
                .spec(resSpec)
                .statusCode(200)
                .body("content.size()", is(2))
                .body("content[0].id", is(3))
                .body("content[0].price", is(30.0f))
                .body("content[0].is_active", is(true))
                .body("content[0].apartment.id", is(3))
                .body("content[0].apartment.street", is("C++"))
                .body("content[0].apartment.city", is("Moscow"))
                .body("content[0].apartment.apartment_type", is("THREE"))
                .body("content[0].description", is("Три"))
                .body("content[1].id", is(2))
                .body("content[1].price", is(20.0f))
                .body("content[1].is_active", is(true))
                .body("content[1].apartment.id", is(2))
                .body("content[1].apartment.street", is("Kotlin"))
                .body("content[1].apartment.city", is("Moscow"))
                .body("content[1].apartment.apartment_type", is("TWO"))
                .body("content[1].description", is("Два"));
    }

    @Test
    @DisplayName("Получить все объявления из города Tula")
    @Sql(value = "/createAdvertAndApartment.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/clearApartmentOnCascade.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAllAdvertsByNameCityInTula() {
        RestAssured
                .given()
                .spec(reqSpec)
                .get("/advert?city=Tula")
                .then()
                .spec(resSpec)
                .statusCode(200)
                .body("content.size()", equalTo(1))
                .body("content[0].id", equalTo(1))
                .body("content[0].price", equalTo(10.0f))
                .body("content[0].is_active", equalTo(true))
                .body("content[0].apartment.id", equalTo(1))
                .body("content[0].apartment.city", equalTo("Tula"))
                .body("content[0].description", equalTo("Один"));
    }
}
