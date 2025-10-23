package edu.rutmiit.demo.fit_contract.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;

public record GymRequest(

        @NotBlank(message = "Адрес обязателен")
        String address,

        @NotNull(message = "Вместительность обязательна")
        @Positive(message = "Вместительность должна быть положительным числом")
        Integer capacity,

        List<String> amenities
) {}