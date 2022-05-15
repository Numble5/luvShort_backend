package com.example.backend.repository;

import com.example.backend.domain.user.Profile;
import com.example.backend.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
}
