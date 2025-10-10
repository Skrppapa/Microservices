package edu.rutmiit.demo.fitapicontract.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ClientRequest(
        @NotBlank(message = "Имя клиента не может быть пустым") String firstName,
        @NotBlank(message = "Фамилия клиента не может быть пустой") String lastName
        @NotNull
        // Добавить дату рождения
) {}