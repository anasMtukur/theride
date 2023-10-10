package com.anas.theride.authlog;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LogLoginRepository extends JpaRepository<LogLogin, UUID> {

}
