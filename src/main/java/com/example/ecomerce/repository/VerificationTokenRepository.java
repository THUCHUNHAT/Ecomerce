package com.example.ecomerce.repository;

import com.example.ecomerce.entity.Verification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface VerificationTokenRepository extends JpaRepository<Verification,Integer> {
    boolean existsByToken( String token);

    @Query("SELECT v FROM Verification v WHERE v.token = :token")
    Optional<Verification> findByToken(@Param("token") String token);

}
