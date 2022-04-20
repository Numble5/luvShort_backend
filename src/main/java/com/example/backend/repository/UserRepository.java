package com.example.backend.repository;

import com.example.backend.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // 이미 가입한 사용자인지 확인할 때 이메일을 이용
    Optional<User> findByEmail(String email);
}
