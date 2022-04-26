package com.example.backend.repository;

import com.example.backend.domain.user.Interest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface InterestRepository  extends JpaRepository<Interest, Long> {

    @Query("select i from Interest i where i.interestName = :interestName")
    Optional<Interest> findByInterestName(String interestName);
}
