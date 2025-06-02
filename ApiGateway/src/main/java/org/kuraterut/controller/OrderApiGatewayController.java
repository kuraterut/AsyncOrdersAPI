package org.kuraterut.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.kuraterut.config.properties.OrderServiceProperties;
import org.kuraterut.dto.OrderRequest;
import org.kuraterut.dto.OrderResponse;
import org.kuraterut.dto.ErrorResponse;
import org.kuraterut.order.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/order")
public class OrderApiGatewayController {

    private final WebClient orderWebClient;
    private final OrderServiceProperties orderServiceProperties;
    private final ObjectMapper objectMapper;

    @Autowired
    public OrderApiGatewayController(@Qualifier("orderWebClient")WebClient orderWebClient,
                                     OrderServiceProperties orderServiceProperties, ObjectMapper objectMapper) {
        this.orderWebClient = orderWebClient;
        this.orderServiceProperties = orderServiceProperties;
        this.objectMapper = objectMapper;
    }

    @PostMapping
    public Mono<ResponseEntity<Object>> createOrder(@RequestBody OrderRequest orderRequest) {
        return orderWebClient.post()
                .uri("/api/order")
                .body(BodyInserters.fromValue(orderRequest))
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
                .map(orderResponse -> ResponseEntity.ok().body(orderResponse))
                .onErrorResume(WebClientRequestException.class, e ->
                        Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                                .body(new ErrorResponse(HttpStatus.SERVICE_UNAVAILABLE,
                                        "Order service is unavailable: " + e.getMessage())))
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
    public Mono<ResponseEntity<Object>> getAllOrders() {
        return orderWebClient.get()
                .uri("/api/order")
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
                .map(orderResponse -> ResponseEntity.ok().body(orderResponse))
                .onErrorResume(WebClientRequestException.class, e ->
                        Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                                .body(new ErrorResponse(HttpStatus.SERVICE_UNAVAILABLE,
                                        "Order service is unavailable: " + e.getMessage())))
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
    public Mono<ResponseEntity<Object>> getOrderById(@PathVariable("id") Long id) {
        return orderWebClient.get()
                .uri("/api/order/{id}", id)
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
                .map(orderResponse -> ResponseEntity.ok().body(orderResponse))
                .onErrorResume(WebClientRequestException.class, e ->
                        Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                                .body(new ErrorResponse(HttpStatus.SERVICE_UNAVAILABLE,
                                        "Order service is unavailable: " + e.getMessage())))
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
    public Mono<ResponseEntity<Object>> getOrdersByUserId(@RequestParam("userId") Long userId) {
        return orderWebClient.get()
                .uri("/api/order/user?userId={userId}", userId)
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
                .map(orderResponse -> ResponseEntity.ok().body(orderResponse))
                .onErrorResume(WebClientRequestException.class, e ->
                        Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                                .body(new ErrorResponse(HttpStatus.SERVICE_UNAVAILABLE,
                                        "Order service is unavailable: " + e.getMessage())))
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

    @GetMapping("/status")
    public Mono<ResponseEntity<Object>> getOrdersByStatus(@RequestParam("status") OrderStatus status) {
        return orderWebClient.get()
                .uri("/api/order/status?status={status}", status)
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
                .map(orderResponse -> ResponseEntity.ok().body(orderResponse))
                .onErrorResume(WebClientRequestException.class, e ->
                        Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                                .body(new ErrorResponse(HttpStatus.SERVICE_UNAVAILABLE,
                                        "Order service is unavailable: " + e.getMessage())))
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
