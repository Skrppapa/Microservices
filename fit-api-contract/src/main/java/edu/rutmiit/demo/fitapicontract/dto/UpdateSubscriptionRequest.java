package edu.rutmiit.demo.fitapicontract.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// DTO для обновления. Не позволяем менять автора книги, только название и ISBN.
public record UpdateSubscriptionRequest(
        @NotBlank(message = "Период не может быть пустым")
        String length,
        @Size(min = 10, max = 13, message = "Код абонемента должен содержать от 10 до 13 символов")
        String code
) {}