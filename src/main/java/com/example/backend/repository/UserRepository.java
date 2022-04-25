package com.example.backend.repository;

import com.example.backend.domain.user.User;
import com.example.backend.domain.user.enums.SocialAccountType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // 이미 가입한 사용자인지 확인할 때 이메일을 이용
    Optional<User> findByEmail(String email);

    // 해당 이메일이 User 엔티티에 있는지 확인
    Boolean existsByEmail(String email);

    Boolean existsByNickname(String nickname);

    // FIXME: JPQL문 맞는지 확인 안해봄
    @Query("select (count(u) = 1) from User u where u.userInfo.socialAccountType = :socialAccountType and u.userInfo.socialId = :socialId")
    Boolean existsBySocialAccountTypeAndSocialId(SocialAccountType socialAccountType, Long socialId);

    @Query("select u.userInfo.socialId from User u where u.userInfo.socialAccountType = :socialAccountType and u.userInfo.socialId = :socialId")
    Long findSocialIdBySocialAccountTypeAndSocialId(SocialAccountType socialAccountType, Long socialId);
}
