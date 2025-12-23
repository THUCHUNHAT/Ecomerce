package com.example.ecomerce.repository;
import com.example.ecomerce.entity.User;
import com.example.ecomerce.enums.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    boolean existsByUsernameAndIdNot(String username, int id);
    boolean existsByEmailAndIdNot(String email, int id);
    boolean existsByPhoneAndIdNot(String phone, int id);
    boolean existsByUsername( String username);
    boolean existsByEmail( String email);
    boolean existsByPhone( String phone);

    @Query("SELECT COUNT(u.id) FROM User u")
    Integer getTotalUsers();

    @Query("SELECT u FROM User u " +
            "WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "   OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "   OR LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "   OR LOWER(u.phone) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<User> searchUsers(@Param("keyword") String keyword, Pageable pageable);

    Optional<User> findByUsername(String username);


    @Query("SELECT u FROM User u WHERE u.email = ?1")
    Optional<User> findByEmail(String email);


    void deleteAllByStatus(UserStatus status);
}
