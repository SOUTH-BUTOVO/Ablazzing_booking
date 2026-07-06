package com.javaacademy.flat_rent.mock;

import com.javaacademy.flat_rent.controller.ApartmentController;
import com.javaacademy.flat_rent.dto.ApartmentRqDto;
import com.javaacademy.flat_rent.dto.ApartmentRsDto;
import com.javaacademy.flat_rent.entity.RoomCount;
import com.javaacademy.flat_rent.service.ApartmentService;
import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ApartmentController.class) // поднимется только ApartmentController и MVC инфраструктура.
public class ApartmentControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    ApartmentService apartmentService; // Замокан только сервис Контроллер тестируется изолированно.

    private final ApartmentRqDto rqDto = new ApartmentRqDto(
            null,
            "Orel",
            "First",
            "1",
            "1A",
            RoomCount.ONE);

    private final ApartmentRsDto rsDto = new ApartmentRsDto(
            1L,
            "Orel",
            "First",
            "1",
            "1A",
            RoomCount.ONE);

    @Test
    @SneakyThrows
    @DisplayName("Создание квартиры")
    public void createApartmentSuccess() {
        Mockito.when(apartmentService.save(rqDto)).thenReturn(rsDto);

        // Проверяется сериализация ответа, то что сохранилось в БД с заполненным id.
        MvcResult result = mockMvc.perform(post("/apartment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(rqDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.city").value("Orel"))
                .andExpect(jsonPath("$.street").value("First"))
                .andExpect(jsonPath("$.house").value("1"))
                .andExpect(jsonPath("$.corpus").value("1A"))
                .andExpect(jsonPath("$.apartment_type").value("ONE"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn();
        System.out.printf("Result: %s", result.getResponse().getContentAsString());

        // Проверять DTO через ArgumentCaptor, то что пришло и id ещё нет (null). Это иногда ловит проблемы с:
        // @JsonProperty @JsonNaming @JsonAlias
        ArgumentCaptor<ApartmentRqDto> captor = ArgumentCaptor.forClass(ApartmentRqDto.class);
        Mockito.verify(apartmentService).save(captor.capture());
        ApartmentRqDto dto = captor.getValue();
        Assertions.assertThat(dto.id()).isNull();
        Assertions.assertThat(dto.city()).isEqualTo("Orel");
        Assertions.assertThat(dto.street()).isEqualTo("First");
        Assertions.assertThat(dto.house()).isEqualTo("1");
        Assertions.assertThat(dto.corpus()).isEqualTo("1A");
        Assertions.assertThat(dto.roomCount()).hasToString("ONE"); // или RC.ONE

        // Проверяет, что на мок-объекте advertService был вызван метод save ровно один раз с аргументом, который
        // равен rqDto (сравнение через equals). Если save не вызывался — тест упадёт с ошибкой. Если вызывался
        // больше одного раза — тест тоже упадёт.
        // Если у DTO equals не переопределён корректно, проверка может не сработать даже при «одинаковых» данных.
        Mockito.verify(apartmentService).save(rqDto);

        // Проверяет, что никакие другие методы на мок-объекте advertService, кроме тех, которые уже были проверены
        // через verify (или использованы в stubbing), не вызывались. Как читать вслух: «Убеждаюсь, что сервис
        // объявлений больше ничего не делал». Нюансы: Это «глобальная» проверка для данного мока. Она смотрит на
        // все взаимодействия с этим объектом за время теста. Часто используют в конце теста как страховку от лишних
        // вызовов. Не используй verifyNoMoreInteractions без необходимости. Он хрупкий: любое изменение реализации
        // (даже логирование через сервис) ломает тест. Лучше проверять только важные вызовы.
        Mockito.verifyNoMoreInteractions(apartmentService);

        // Если переиспользуешь мок между тестами — сбрасывай его: Mockito.reset(advertService).
        // Но лучше создавать новый мок на каждый тест.

        // Как правильно читать verify в целом, Шаблон чтения:
        // «Проверяю, что на [мок-объекте] был вызван метод [имя] [количество раз]
        // с аргументами [описание аргументов]».
        // Количество раз по умолчанию — один раз. Если нужно другое — указывают явно (см. ниже).
        // Другие полезные варианты verify Изменение количества вызовов:
        // 0 раз - Mockito.verify(advertService, Mockito.never()).save(rqDto);
        // минимум 1 раз - Mockito.verify(advertService, Mockito.atLeastOnce()).save(rqDto);
        // ровно 2 раза - Mockito.verify(advertService, Mockito.times(2)).save(rqDto);
        // хотя бы 3 раза - Mockito.verify(advertService, Mockito.atLeast(3)).save(rqDto);
        // не более 5 раз - Mockito.verify(advertService, Mockito.atMost(5)).save(rqDto);
        // Вместо any() можно использовать более строгие матчеры из ArgumentMatchers:
        // verify(advertService).save(argThat(dto -> dto.getTitle().contains("iPhone")));
        // verify(advertService).findAllByCity(eq("Moscow"), same(pageable));
        // verify(advertService).update(captor.capture()); // см. про ArgumentCaptor ниже
        // eq(value) — явное указание «должно быть равно этому значению».
        // argThat(predicate) — проверка по условию.
        // same(obj) — проверка на ссылочное равенство (==).
        // nullable(String.class) — любой объект или null.
    }
}
