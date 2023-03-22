package com.vladimirpandurov.serverManagerApp2.repository;

import com.vladimirpandurov.serverManagerApp2.domain.Server;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServerRepository extends JpaRepository<Server, Long> {
    Server findByIpAddress(String ipAddress);
}
