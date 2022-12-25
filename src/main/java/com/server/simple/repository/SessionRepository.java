package com.server.simple.repository;

import com.server.simple.domain.Session;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessionRepository extends CrudRepository<Session,Long> {

    Optional<Session> findByAccessToken(String accessToken);
}
