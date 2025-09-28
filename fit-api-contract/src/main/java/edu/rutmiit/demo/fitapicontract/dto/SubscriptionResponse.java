package edu.rutmiit.demo.fitapicontract.dto;

import java.time.LocalDateTime;

public record SubscriptionResponse(
        Long id,
        String length,
        String code,
        ClientResponse client,
        LocalDateTime createdAt
) {}
