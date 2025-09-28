package edu.rutmiit.demo.fitapicontract.endpoints;

import edu.rutmiit.demo.fitapicontract.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "subscriptions", description = "API для работы с абонементами")
@RequestMapping("/api/subscriptions")
public interface SubscriptionApi {

    @Operation(summary = "Получить абонемент по ID")
    @ApiResponse(responseCode = "200", description = "Абонемент найден")
    @ApiResponse(responseCode = "404", description = "абонемент не найден", content = @Content(schema = @Schema(implementation = StatusResponse.class)))
    @GetMapping("/{id}")
    SubscriptionResponse getSubscriptionById(@PathVariable("id") Long id);

    @Operation(summary = "Получить список всех абонементов с фильтрацией и пагинацией")
    @ApiResponse(responseCode = "200", description = "Список абонементов")
    @GetMapping
    PagedResponse<SubscriptionResponse> getAllSubscriptions(
            @Parameter(description = "Фильтр по ID клиента") @RequestParam(required = false) Long clientId, // поменял название
            @Parameter(description = "Номер страницы (0..N)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Размер страницы") @RequestParam(defaultValue = "10") int size
    );

    @Operation(summary = "Создать новый абонемент")
    @ApiResponse(responseCode = "201", description = "Абонемент успешно создан")
    @ApiResponse(responseCode = "400", description = "Невалидный запрос", content = @Content(schema = @Schema(implementation = StatusResponse.class)))
    @ApiResponse(responseCode = "409", description = "Абонемент с таким кодом уже существует", content = @Content(schema = @Schema(implementation = StatusResponse.class)))
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    SubscriptionResponse createSubscription(@Valid @RequestBody SubscriptionRequest request);

    @Operation(summary = "Обновить абонемент по ID")
    @ApiResponse(responseCode = "200", description = "Абонемент успешно обновлен")
    @ApiResponse(responseCode = "404", description = "Абонемент не найден", content = @Content(schema = @Schema(implementation = StatusResponse.class)))
    @ApiResponse(responseCode = "409", description = "Абонемент с таким кодом уже существует", content = @Content(schema = @Schema(implementation = StatusResponse.class)))
    @PutMapping("/{id}")
    SubscriptionResponse updateSubscription(@PathVariable Long id, @Valid @RequestBody UpdateSubscriptionRequest request);

    @Operation(summary = "Удалить абонемент по ID")
    @ApiResponse(responseCode = "204", description = "Абонемент успешно удален")
    @ApiResponse(responseCode = "404", description = "Абонемент не найден")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteSubscription(@PathVariable Long id);
}