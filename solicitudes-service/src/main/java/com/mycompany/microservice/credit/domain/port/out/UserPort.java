package com.mycompany.microservice.credit.domain.port.out;

public interface UserPort {
    boolean userExists(Long userId);
}
