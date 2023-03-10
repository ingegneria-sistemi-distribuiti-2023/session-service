package com.isd.session.repository;

import com.isd.session.domain.Session;
import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends JpaRepository<Session, String> {
    Session findOneBySessionId(Integer id);
    Session findOneByUserId(Integer id);
    void deleteBySessionId(Integer sessionId);
}