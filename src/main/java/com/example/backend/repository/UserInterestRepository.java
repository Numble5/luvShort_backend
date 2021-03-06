package com.example.backend.repository;

import com.example.backend.domain.user.User;
import com.example.backend.domain.user.UserInterest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserInterestRepository extends JpaRepository<UserInterest, Long> {

    @Query("select u from UserInterest u where u.user = :user")
    List<UserInterest> findUserInterestsByUser(User user);

    @Transactional
    @Modifying
    @Query("delete from UserInterest u where u.user = :user")
    void deleteAllByUser(User user);
}
