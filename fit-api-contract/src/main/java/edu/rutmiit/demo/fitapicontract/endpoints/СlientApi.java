package edu.rutmiit.demo.fitapicontract.endpoints;

import edu.rutmiit.demo.fitapicontract.dto.ClientRequest;
import edu.rutmiit.demo.fitapicontract.dto.ClientResponse;
import edu.rutmiit.demo.fitapicontract.dto.StatusResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "clients", description = "API для работы с клиентами")
@RequestMapping("/api/clients")
public interface СlientApi {

    @Operation(summary = "Получить всех клиентов")
    @ApiResponse(responseCode = "200", description = "Список клиентов")
    @GetMapping
    List<ClientResponse> getAllClients();

    @Operation(summary = "Получить клиента по ID")
    @ApiResponse(responseCode = "200", description = "Клиент найден")
    @ApiResponse(responseCode = "404", description = "Клиент не найден", content = @Content(schema = @Schema(implementation = StatusResponse.class)))
    @GetMapping("/{id}")
    ClientResponse getClientById(@PathVariable Long id);

    @Operation(summary = "Создать нового клиента")
    @ApiResponse(responseCode = "201", description = "Клиент успешно создан")
    @ApiResponse(responseCode = "400", description = "Невалидный запрос", content = @Content(schema = @Schema(implementation = StatusResponse.class)))
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ClientResponse createClient(@Valid @RequestBody ClientRequest request);

    @Operation(summary = "Обновить клиента")
    @ApiResponse(responseCode = "200", description = "Клиент обновлен")
    @ApiResponse(responseCode = "404", description = "Клиент не найден", content = @Content(schema = @Schema(implementation = StatusResponse.class)))
    @PutMapping("/{id}")
    ClientResponse updateClient(@PathVariable Long id, @Valid @RequestBody ClientRequest request);

    @Operation(summary = "Удалить клиента")
    @ApiResponse(responseCode = "204", description = "Клиент удален")
    @ApiResponse(responseCode = "404", description = "Клиент не найден")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteClient(@PathVariable Long id);
}
