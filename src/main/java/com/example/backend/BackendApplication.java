package com.example.backend;

import com.example.backend.domain.user.Interest;
import com.example.backend.repository.InterestRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.IntStream;

@EnableJpaAuditing // NOTE: JPA Auditing 활성화해야
@SpringBootApplication
@Slf4j
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    @Transactional
    @Bean
    public CommandLineRunner runner(InterestRepository interestRepository) {
        return (args) -> {

            // Interest 엔티티 추가
            String[] interests = new String[]{"여행", "쇼핑", "스포츠", "영화", "게임", "음악", "반려동물", "독서", "요리"};
            for (String i: interests){
                Interest interest = interestRepository.save(Interest.builder()
                        .interestName(i)
                        .build());
            }
        };
    }
}
