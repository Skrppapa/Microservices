package edu.rutmiit.demo.fitapicontract.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SubscriptionRequest(
        @NotBlank(message = "Период действия абонемента не может быть пустым")
        String length,
        @Size(min = 10, max = 13, message = "Код абонемента должен содержать от 10 до 13 символов")
        String code,
        @NotNull(message = "ID клиента не может быть пустым")
        Long clientId
) {}