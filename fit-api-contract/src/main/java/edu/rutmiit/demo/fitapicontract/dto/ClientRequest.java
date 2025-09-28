package edu.rutmiit.demo.fitapicontract.dto;

import jakarta.validation.constraints.NotBlank;

public record ClientRequest(
        @NotBlank(message = "Имя клиента не может быть пустым") String firstName,
        @NotBlank(message = "Фамилия клиента не может быть пустой") String lastName
        // Добавить дату рождения
) {}