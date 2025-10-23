package edu.rutmiit.demo.fit_contract.endpoints;

import edu.rutmiit.demo.fit_contract.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "gyms", description = "API для работы с тренажерными залами")
@RequestMapping("/api/gyms")
public interface GymApi {

    @Operation(summary = "Получить тренажерный зал по ID")
    @ApiResponse(responseCode = "200", description = "Тренажерный зал найден")
    @ApiResponse(responseCode = "404", description = "Тренажерный зал не найден", content = @Content(schema = @Schema(implementation = StatusResponse.class)))
    @GetMapping("/{id}")
    GymResponse getGymById(@PathVariable("id") Long id);

    @Operation(summary = "Получить список всех тренажерных залов с фильтрацией и пагинацией")
    @ApiResponse(responseCode = "200", description = "Список тренажерных залов")
    @GetMapping
    PagedResponse<GymResponse> getAllGyms(
            @Parameter(description = "Фильтр по адресу") @RequestParam(required = false) String address,
            @Parameter(description = "Фильтр по минимальной вместительности") @RequestParam(required = false) Integer minCapacity,
            @Parameter(description = "Номер страницы (0..N)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Размер страницы") @RequestParam(defaultValue = "10") int size
    );

    @Operation(summary = "Создать новый тренажерный зал")
    @ApiResponse(responseCode = "201", description = "Тренажерный зал успешно создан")
    @ApiResponse(responseCode = "400", description = "Невалидный запрос", content = @Content(schema = @Schema(implementation = StatusResponse.class)))
    @ApiResponse(responseCode = "409", description = "Тренажерный зал с таким адресом уже существует", content = @Content(schema = @Schema(implementation = StatusResponse.class)))
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    GymResponse createGym(@Valid @RequestBody GymRequest request);

    @Operation(summary = "Обновить тренажерный зал по ID")
    @ApiResponse(responseCode = "200", description = "Тренажерный зал успешно обновлен")
    @ApiResponse(responseCode = "404", description = "Тренажерный зал не найден", content = @Content(schema = @Schema(implementation = StatusResponse.class)))
    @ApiResponse(responseCode = "409", description = "Тренажерный зал с таким адресом уже существует", content = @Content(schema = @Schema(implementation = StatusResponse.class)))
    @PutMapping("/{id}")
    GymResponse updateGym(@PathVariable Long id, @Valid @RequestBody GymRequest request);

    @Operation(summary = "Удалить тренажерный зал по ID")
    @ApiResponse(responseCode = "204", description = "Тренажерный зал успешно удален")
    @ApiResponse(responseCode = "404", description = "Тренажерный зал не найден")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteGym(@PathVariable Long id);
}