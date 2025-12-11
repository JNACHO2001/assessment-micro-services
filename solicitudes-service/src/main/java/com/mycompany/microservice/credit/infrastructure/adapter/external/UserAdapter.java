package com.mycompany.microservice.credit.infrastructure.adapter.external;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.mycompany.microservice.credit.domain.port.out.UserPort;

@Component
public class UserAdapter implements UserPort {

    private final WebClient webClient;

    public UserAdapter(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("lb://AUTH-SERVICE").build();
    }

    @Override
    public boolean userExists(Long userId) {
        try {
            return Boolean.TRUE.equals(webClient.get()
                    .uri("/api/auth/users/{id}", userId)
                    .retrieve()
                    .toBodilessEntity()
                    .map(response -> response.getStatusCode().is2xxSuccessful())
                    .onErrorReturn(false)
                    .block());
        } catch (Exception e) {
            return false;
        }
    }
}
