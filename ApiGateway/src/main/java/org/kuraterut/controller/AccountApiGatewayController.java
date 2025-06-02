package org.kuraterut.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.kuraterut.config.properties.AccountServiceProperties;
import org.kuraterut.dto.AccountRequest;
import org.kuraterut.dto.AccountResponse;
import org.kuraterut.dto.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/account")
public class AccountApiGatewayController {
    private final WebClient accountWebClient;
    private final AccountServiceProperties accountServiceProperties;
    private final ObjectMapper objectMapper;

    @Autowired
    public AccountApiGatewayController(@Qualifier("accountWebClient") WebClient accountWebClient,
                                       AccountServiceProperties accountServiceProperties, ObjectMapper objectMapper) {
        this.accountWebClient = accountWebClient;
        this.accountServiceProperties = accountServiceProperties;
        this.objectMapper = objectMapper;
    }

    @PostMapping
    public Mono<ResponseEntity<Object>> createAccount(@RequestBody AccountRequest accountRequest) {

        return accountWebClient.post()
                .uri("/api/account")
                .body(BodyInserters.fromValue(accountRequest))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        clientResponse -> clientResponse.bodyToMono(String.class)
                                .flatMap(error -> Mono.error(new ResponseStatusException(clientResponse.statusCode(), error)))
                )
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse ->
                        clientResponse.bodyToMono(String.class)
                                .flatMap(error -> Mono.error(new ResponseStatusException(clientResponse.statusCode(), error)))
                )
                .bodyToMono(Object.class)
                .map(accountResponse -> ResponseEntity.ok().body(accountResponse))
                .onErrorResume(WebClientRequestException.class, e ->
                        Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                                .body(new ErrorResponse(HttpStatus.SERVICE_UNAVAILABLE,
                                        "Account service is unavailable: " + e.getMessage())))
                )
                .onErrorResume(ResponseStatusException.class, e ->
                        {
                            try {
                                return Mono.just(ResponseEntity.status(e.getStatusCode())
                                        .body(objectMapper.readValue(e.getReason(), ErrorResponse.class)));
                            } catch (JsonProcessingException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                )
                .onErrorResume(Exception.class, e ->
                        Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                                        "Unexpected error: " + e.getMessage()))
                        ));
    }

    @GetMapping
    public Mono<ResponseEntity<Object>> getAllAccounts() {
        return accountWebClient.get()
                .uri("/api/account")
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        clientResponse -> clientResponse.bodyToMono(String.class)
                                .flatMap(error -> Mono.error(new ResponseStatusException(clientResponse.statusCode(), error)))
                )
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse ->
                        clientResponse.bodyToMono(String.class)
                                .flatMap(error -> Mono.error(new ResponseStatusException(clientResponse.statusCode(), error)))
                )
                .bodyToMono(Object.class)
                .map(accountResponse -> ResponseEntity.ok().body(accountResponse))
                .onErrorResume(WebClientRequestException.class, e ->
                        Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                                .body(new ErrorResponse(HttpStatus.SERVICE_UNAVAILABLE,
                                        "Account service is unavailable: " + e.getMessage())))
                )
                .onErrorResume(ResponseStatusException.class, e ->
                        {
                            try {
                                return Mono.just(ResponseEntity.status(e.getStatusCode())
                                        .body(objectMapper.readValue(e.getReason(), ErrorResponse.class)));
                            } catch (JsonProcessingException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                )
                .onErrorResume(Exception.class, e ->
                        Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                                        "Unexpected error: " + e.getMessage()))
                        ));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Object>> getAccountById(@PathVariable("id") Long id) {
        return accountWebClient.get()
                .uri("/api/account/{id}", id)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        clientResponse -> clientResponse.bodyToMono(String.class)
                                .flatMap(error -> Mono.error(new ResponseStatusException(clientResponse.statusCode(), error)))
                )
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse ->
                        clientResponse.bodyToMono(String.class)
                                .flatMap(error -> Mono.error(new ResponseStatusException(clientResponse.statusCode(), error)))
                )
                .bodyToMono(Object.class)
                .map(accountResponse -> ResponseEntity.ok().body(accountResponse))
                .onErrorResume(WebClientRequestException.class, e ->
                        Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                                .body(new ErrorResponse(HttpStatus.SERVICE_UNAVAILABLE,
                                        "Account service is unavailable: " + e.getMessage())))
                )
                .onErrorResume(ResponseStatusException.class, e ->
                        {
                            try {
                                return Mono.just(ResponseEntity.status(e.getStatusCode())
                                        .body(objectMapper.readValue(e.getReason(), ErrorResponse.class)));
                            } catch (JsonProcessingException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                )
                .onErrorResume(Exception.class, e ->
                        Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                                        "Unexpected error: " + e.getMessage()))
                        ));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Object>> deleteAccountById(@PathVariable("id") Long id) {
        return accountWebClient.delete()
                .uri("/api/account/{id}", id)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        clientResponse -> clientResponse.bodyToMono(String.class)
                                .flatMap(error -> Mono.error(new ResponseStatusException(clientResponse.statusCode(), error)))
                )
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse ->
                        clientResponse.bodyToMono(String.class)
                                .flatMap(error -> Mono.error(new ResponseStatusException(clientResponse.statusCode(), error)))
                )
                .bodyToMono(Object.class)
                .map(accountResponse -> ResponseEntity.ok().body(accountResponse))
                .onErrorResume(WebClientRequestException.class, e ->
                        Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                                .body(new ErrorResponse(HttpStatus.SERVICE_UNAVAILABLE,
                                        "Account service is unavailable: " + e.getMessage())))
                )
                .onErrorResume(ResponseStatusException.class, e ->
                        {
                            try {
                                return Mono.just(ResponseEntity.status(e.getStatusCode())
                                        .body(objectMapper.readValue(e.getReason(), ErrorResponse.class)));
                            } catch (JsonProcessingException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                )
                .onErrorResume(Exception.class, e ->
                        Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                                        "Unexpected error: " + e.getMessage()))
                        ));
    }


    @GetMapping("/user")
    public Mono<ResponseEntity<Object>> getAccountByUserId(@RequestParam("userId") Long userId) {
        return accountWebClient.get()
                .uri("/api/account/user?userId={userId}", userId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        clientResponse -> clientResponse.bodyToMono(String.class)
                                .flatMap(error -> Mono.error(new ResponseStatusException(clientResponse.statusCode(), error)))
                )
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse ->
                        clientResponse.bodyToMono(String.class)
                                .flatMap(error -> Mono.error(new ResponseStatusException(clientResponse.statusCode(), error)))
                )
                .bodyToMono(Object.class)
                .map(accountResponse -> ResponseEntity.ok().body(accountResponse))
                .onErrorResume(WebClientRequestException.class, e ->
                        Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                                .body(new ErrorResponse(HttpStatus.SERVICE_UNAVAILABLE,
                                        "Account service is unavailable: " + e.getMessage())))
                )
                .onErrorResume(ResponseStatusException.class, e ->
                        {
                            try {
                                return Mono.just(ResponseEntity.status(e.getStatusCode())
                                        .body(objectMapper.readValue(e.getReason(), ErrorResponse.class)));
                            } catch (JsonProcessingException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                )
                .onErrorResume(Exception.class, e ->
                        Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                                        "Unexpected error: " + e.getMessage()))
                        ));
    }

    @PutMapping("/user/add")
    public Mono<ResponseEntity<Object>> addMoneyByUserId(@RequestParam("userId") Long userId, @RequestParam("amount") BigDecimal amount) {
        return accountWebClient.put()
                .uri("/api/account/user/add?userId={userId}&amount={amount}", userId, amount)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        clientResponse -> clientResponse.bodyToMono(String.class)
                                .flatMap(error -> Mono.error(new ResponseStatusException(clientResponse.statusCode(), error)))
                )
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse ->
                        clientResponse.bodyToMono(String.class)
                                .flatMap(error -> Mono.error(new ResponseStatusException(clientResponse.statusCode(), error)))
                )
                .bodyToMono(Object.class)
                .map(accountResponse -> ResponseEntity.ok().body(accountResponse))
                .onErrorResume(WebClientRequestException.class, e ->
                        Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                                .body(new ErrorResponse(HttpStatus.SERVICE_UNAVAILABLE,
                                        "Account service is unavailable: " + e.getMessage())))
                )
                .onErrorResume(ResponseStatusException.class, e ->
                        {
                            try {
                                return Mono.just(ResponseEntity.status(e.getStatusCode())
                                        .body(objectMapper.readValue(e.getReason(), ErrorResponse.class)));
                            } catch (JsonProcessingException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                )
                .onErrorResume(Exception.class, e ->
                        Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                                        "Unexpected error: " + e.getMessage()))
                        ));
    }

    @PutMapping("/user/remove")
    public Mono<ResponseEntity<Object>> removeMoneyByUserId(@RequestParam("userId") Long userId, @RequestParam("amount") BigDecimal amount) {
        return accountWebClient.put()
                .uri("/api/account/user/remove?userId={userId}&amount={amount}", userId, amount)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        clientResponse -> clientResponse.bodyToMono(String.class)
                                .flatMap(error -> Mono.error(new ResponseStatusException(clientResponse.statusCode(), error)))
                )
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse ->
                        clientResponse.bodyToMono(String.class)
                                .flatMap(error -> Mono.error(new ResponseStatusException(clientResponse.statusCode(), error)))
                )
                .bodyToMono(Object.class)
                .map(accountResponse -> ResponseEntity.ok().body(accountResponse))
                .onErrorResume(WebClientRequestException.class, e ->
                        Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                                .body(new ErrorResponse(HttpStatus.SERVICE_UNAVAILABLE,
                                        "Account service is unavailable: " + e.getMessage())))
                )
                .onErrorResume(ResponseStatusException.class, e ->
                        {
                            try {
                                return Mono.just(ResponseEntity.status(e.getStatusCode())
                                        .body(objectMapper.readValue(e.getReason(), ErrorResponse.class)));
                            } catch (JsonProcessingException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                )
                .onErrorResume(Exception.class, e ->
                        Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                                        "Unexpected error: " + e.getMessage()))
                        ));
    }

}
