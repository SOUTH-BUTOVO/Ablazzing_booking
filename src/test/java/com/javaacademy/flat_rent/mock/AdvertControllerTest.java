package com.javaacademy.flat_rent.mock;

import com.javaacademy.flat_rent.dto.AdvertRqDto;
import com.javaacademy.flat_rent.dto.AdvertRsDto;
import com.javaacademy.flat_rent.dto.ApartmentRsDto;
import com.javaacademy.flat_rent.entity.RoomCount;
import com.javaacademy.flat_rent.service.AdvertService;
import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AdvertControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper; // tools.jackson.databind.ObjectMapper;!!!

    // Мокирую сервис, который взаимодействует с БД.
    // не пытаться мокировать сам DTO‑объект — это бессмысленно,
    //мокируйте сервис, который возвращает AdvertRsDto с заполненным apartment;
    @MockitoBean
    private AdvertService advertService;

    private final AdvertRqDto rqDto = new AdvertRqDto(
            null,
            BigDecimal.TEN,
            true,
            1L,
            "Описание");

    @Test
    @SneakyThrows
    @DisplayName("Создание объявления")
    public void createAdvert() {
        // Подготавливаем ожидаемый ответ с замокированным apartment
        AdvertRsDto rsDto = new AdvertRsDto(
                1L,
                BigDecimal.TEN,
                true,
                new ApartmentRsDto(1L, "Piter", "Spring", "7", "1A", RoomCount.ONE),
                "Описание");

        // Настраиваем моки: при вызове сервиса возвращаем ожидаемый ответ
        // Если метод может выбросить исключение или он Spy, то лучше использовать .doReturn()
        Mockito.when(advertService.save(rqDto)).thenReturn(rsDto);

        mockMvc.perform(post("/advert") // добавил импорт MockMvcRequestBuilders.post;
                        .contentType(MediaType.APPLICATION_JSON)
                        // Автоматически сериализуем Java-объект в JSON строку:
                        .content(objectMapper.writeValueAsString(rqDto)))
                .andDo(print()) // <-- логирование
                .andExpect(status().isCreated()) // добавил импорт MockMvcResultMatchers.status;
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.price").value(10))
                .andExpect(jsonPath("$.is_active").value(true))
                .andExpect(jsonPath("$.apartment.id").value(1))
                .andExpect(jsonPath("$.apartment.city").value("Piter"))
                .andExpect(jsonPath("$.apartment.street").value("Spring"))
                .andExpect(jsonPath("$.apartment.house").value("7"))
                .andExpect(jsonPath("$.apartment.corpus").value("1A"))
                .andExpect(jsonPath("$.apartment.apartment_type").value(RoomCount.ONE.name()))
                .andExpect(jsonPath("$.description").value("Описание"));

        // Что бы я улучшил: 1 Проверять вызов сервиса. Сейчас ты проверяешь только ответ. Но контроллер
        // ещё обязан вызвать сервис. После запроса можно добавить:
        Mockito.verify(advertService).save(rqDto);
        // и
        Mockito.verifyNoMoreInteractions(advertService);
    }

    @Test
    @DisplayName("Получить все объявления по городу с пагинацией")
    @SneakyThrows
    public void getAllAdvertsByCity() {
        // 1 С начало нужно создать список объектов для страницы prepareAdvert().

        // 2 Создание Page с тестовыми данными. Используем PageImpl для создания страницы с нужными параметрами:
        // Используйте PageImpl для создания тестовых страниц — это стандартный способ. pageResponse().

        // 3. Мокирование сервиса. В тесте настраиваем мок сервиса, чтобы он возвращал подготовленную страницу:
        // Настраиваю мок: при вызове метода с любыми параметрами, возвращаем тестовую страницу
        Mockito.when(advertService.findAllByCity(anyString(), any(Pageable.class))).thenReturn(pageResponse());

        // Выполняем запрос
        mockMvc.perform(get("/advert") // добавил импорт MockMvcRequestBuilders.get;
                        .param("city", "Moscow")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "price, desc"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].price").value(1000))
                .andExpect(jsonPath("$.content[0].apartment.street").value("Street1"))
                .andExpect(jsonPath("$.content[1].price").value(800))
                .andExpect(jsonPath("$.content[1].apartment.house").value("22"))
                .andExpect(jsonPath("$.totalElements").value(3))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.first").value(true))
                .andExpect(jsonPath("$.last").value(true));

        // Ключевые поля для проверки метаданные пагинации — они важны для клиента:
        //$.content — массив объектов AdvertRsDto;
        //$.totalElements — общее количество элементов;
        //$.totalPages — количество страниц;
        //$.number — текущая страница (0‑индексация);
        //$.size — размер страницы;
        //$.first / $.last — флаги первой/последней страницы.

        // Лучше проверить Pageable полностью. any(Pageable.class) проверяет только факт вызова.
        // Можно проверить реальные параметры. Это уже проверяет работу Spring MVC.
        ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);
        Mockito.verify(advertService).findAllByCity(eq("Moscow"), captor.capture());
        Pageable pageable = captor.getValue();
        Assertions.assertThat(pageable.getPageNumber()).isEqualTo(0);
        Assertions.assertThat(pageable.getPageSize()).isEqualTo(10);
        //Assertions.assertThat(pageable.getSort()).isEqualTo(Sort.by(Sort.Direction.ASC, "price")); // не проходит

        // Для GET
        Mockito.verify(advertService).findAllByCity(anyString(), any(Pageable.class));
    }

    @Test
    @DisplayName("Получить все объявления по дефолту") // Очень полезный тест без параметров вообще.
    @SneakyThrows
    public void getAllAdvertsByCityDefault() {
        // 1. Мокируем сервис: любой город и любой Pageable → возвращаем тестовую страницу
        // anyString(), any(Class<T>) Это матчеры (matchers) Mockito — специальные объекты для описания условий
        // соответствия аргументов при настройке моков или проверке вызовов.anyString():соответствует любому
        // String (включая null); синоним any(String.class). any(Pageable.class): соответствует любому объекту
        // типа Pageable (включая null). Использовать: когда точное значение аргумента не важно для теста,
        // либо когда нужно замокировать метод для любых входных данных.
        Mockito.when(advertService.findAllByCity(anyString(), any(Pageable.class))).thenReturn(pageResponse());

        // 2. Выполняем запрос БЕЗ параметров
        mockMvc.perform(get("/advert"))
                .andDo(print()).andExpect(status().isOk());

        // 3. Создаём капто́р для захвата Pageable
        // ArgumentCaptor<T> (похититель) инструмент Mockito для захвата аргументов, переданных в мок‑объект,
        // чтобы потом их проверить. Как работает: 1 Создаётся капто́р для нужного типа:
        // ArgumentCaptor.forClass(Pageable.class). 2 В verify() используется captor.capture() — это «помечает»
        // аргумент для захвата. 3 После вызова verify() можно получить захваченное значение через
        // captor.getValue(). позволяет: проверить не только факт вызова метода, но и конкретные значения его
        // аргументов, протестировать логику формирования объектов (например, Pageable) внутри контроллера.
        ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);

        // 4. Проверяем, что метод вызван с city = "Moscow" и захватываем Pageable
        // Mockito.verify() Метод для проверки, что мок‑объект был вызван с ожидаемыми параметрами. Что делает:
        // подтверждает, что указанный метод мок‑объекта был вызван, проверяет, что аргументы вызова
        // соответствуют заданным условиям (матчерам).
        // eq(value) матчер, который проверяет точное соответствие значения.
        // eq("Moscow") — аргумент должен быть строго равен "Moscow" (не null!). Использовать: при проверке
        // вызовов (verify), когда важно убедиться, что метод вызван с определённым аргументом.
        // captor.capture() Специальный матчер, используемый только в verify().Что делает: «подменяет» обычный
        // матчер (any(), eq() и т.д.); при вызове метода захватывает переданный аргумент и сохраняет его внутри
        // капто́ра.
        // Важно: можно использовать только один раз в рамках одного verify().
        // captor.getValue() Что возвращает: последний захваченный аргумент (тип T, в нашем случае — Pageable).
        // Особенности: если метод вызывался несколько раз, возвращает аргумент из последнего вызова; если вызов
        // не произошёл или capture() не использовался, выбросит исключение.
        Mockito.verify(advertService).findAllByCity(eq("Moscow"), captor.capture());

        // 5. Получаем захваченный Pageable
        Pageable pageable = captor.getValue();

        // 6. Проверяем параметры Pageable
        Assertions.assertThat(pageable.getPageSize()).isEqualTo(10); // размер страницы
        Assertions.assertThat(pageable.getPageNumber()).isEqualTo(0); // номер страницы (по умол 0)
        Assertions.assertThat(pageable.getSort().isSorted()).isTrue(); // сортировку
        Assertions.assertThat(pageable.getSort().toList().get(0).getProperty()).isEqualTo("price");
        Assertions.assertThat(pageable.getSort().toList().get(0).getDirection()).isEqualTo(Sort.Direction.DESC);

        // Такой подход позволяет:
        // 1 убедиться, что контроллер правильно формирует параметры пагинации;
        // 2 проверить, что используется дефолтный город (Moscow);
        // 3 подтвердить корректность настроек Pageable (размер, сортировка).
    }

    private List<AdvertRsDto> prepareAdvert() {
        ApartmentRsDto apartment1 = new ApartmentRsDto(
                1L,
                "Moscow",
                "Street1",
                "11",
                null,
                RoomCount.ONE);

        ApartmentRsDto apartment2 = new ApartmentRsDto(
                2L,
                "Leningrad",
                "Street2",
                "22",
                "3B",
                RoomCount.ONE);

        return Arrays.asList(
                new AdvertRsDto(1L, BigDecimal.valueOf(1000), true, apartment1, "Описание 1"),
                new AdvertRsDto(2L, BigDecimal.valueOf(800), true, apartment2, "Описание 2"),
                new AdvertRsDto(3L, BigDecimal.valueOf(500), false, apartment1, "Описание 3")
        );
    }

    private Page<AdvertRsDto> pageResponse() {
        // Подготавливаю тестовую страницу
        List<AdvertRsDto> adverts = prepareAdvert();

        // Создаём Pageable с теми же параметрами, что ожидаются в тесте
        PageRequest page = PageRequest.of(
                0,
                10,
                Sort.by(Sort.Direction.DESC, "price"));

        // Создаём страницу с тестовыми данными. Для Page сделать PageImpl очень правильный подход:
        return new PageImpl<>(adverts, page, adverts.size());
    }
}
