package com.javaacademy.flat_rent.mock;

import com.javaacademy.flat_rent.controller.ClientController;
import com.javaacademy.flat_rent.service.ClientService;
import jakarta.persistence.EntityNotFoundException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClientController.class)
public class ClientControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockitoBean
    ClientService clientService;

    // В этом методе нет логики «проверить, что клиент существует». Если такая проверка есть она уже в
    // ClientService, и её тестировать надо в юнит-тестах сервиса, а не контроллера.
    // Для контроллера достаточно проверить:
    // Что HTTP-статус 204 No Content.
    // Что clientService.removeById(id) был вызван ровно один раз с правильным id.

    @Test
    @SneakyThrows
    @DisplayName("Удаление клиента по id")
    public void deleteClientSuccess() {
        Long id = 1L;

        mockMvc.perform(delete("/client/{id}", id))
                .andDo(print())
                .andExpect(status().isNoContent())
        // Проверить отсутствие тела ответа для 204 No Content это полезно. Так ты явно проверишь контракт HTTP.
                .andExpect(content().string(""))
        // Для 204 обычно нет тела и нет Content-Type. Иногда проверяют:
                .andExpect(header().doesNotExist("Content-Type"))
        ;

        // Mockito.when(...).thenReturn(...) тут не нужен, потому что removeById это void-метод,
        // и мок по умолчанию ничего не делает (и это нормально).
        //Мы сразу делаем verify, чтобы убедиться, что сервис получил именно тот id, который пришёл в запросе.
        //Используем eq(id) вместо просто id, чтобы явно показать: «проверяем точное совпадение».

        Mockito.verify(clientService).removeById(eq(id));
        Mockito.verifyNoMoreInteractions(clientService);
    }

    // Когда всё-таки может понадобиться «создать» клиента Есть два случая, когда подготовка данных имеет смысл:
    // Если в контроллере есть логика валидации или проверки существования. Например, если контроллер сам
    // проверяет, что клиент есть, и выбрасывает NotFoundException. Тогда в тесте ты можешь настроить мок так:
    // doThrow(new NotFoundException("Client not found")).when(clientService).removeById(anyLong());
    // и проверить, что возвращается 404. Но это уже тест на обработку ошибок, а не на «успешное удаление».
    // 2 Если ты тестируешь интеграционно, с реальной БД (например, @SpringBootTest). Тогда да,
    // можно вставить клиента через SQL/JPA, удалить через API и проверить, что его нет.

//    @Test
//    @SneakyThrows
//    @DisplayName("Не найден клиент при удалении")
//    public void notFoundClientByDelete() {
//        Long id = 2L;
//
//        Mockito.doThrow(new EntityNotFoundException("Client not found"))
//                .when(clientService).removeById(anyLong());
//
//        mockMvc.perform(delete("/client/{id}", id))
//                .andDo(print())
//                .andExpect(status().isNotFound())
//        ;
//    }
}
