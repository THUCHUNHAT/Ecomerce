package com.example.ecomerce.repository;
import com.example.ecomerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {
    boolean existsByUsernameAndIdNot(String username, int id);
    boolean existsByEmailAndIdNot(String email, int id);
    boolean existsByPhoneAndIdNot(String phone, int id);
    boolean existsByUsername( String username);
    boolean existsByEmail( String email);
    boolean existsByPhone( String phone);
}
