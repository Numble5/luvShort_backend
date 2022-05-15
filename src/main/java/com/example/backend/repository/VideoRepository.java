package com.example.backend.repository;

import com.example.backend.domain.user.User;
import com.example.backend.domain.user.enums.GenderType;
import com.example.backend.domain.video.Video;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface VideoRepository extends JpaRepository<Video, Long>{
    List<Video> findAllBy();
    Page<Video> findByIdxLessThan(Long lastId, Pageable page);
    Video findTop1ByOrderByIdxDesc();
    List<Video> findTop10ByOrderByCreatedDateDesc();

    Optional<Video> findByIdx(Long idx);
    Optional<Video> findByUploader(User uploader);

    @Query(value = "select distinct v from Video v\n" +
            "left join VideoCategory vc\n" +
            "on vc.video.idx = v.idx\n"+
            "where v.uploader.userInfo.genderType = :gender\n" +
            "or vc.category.categoryName in (:categories)\n" +
            "order by v.idx desc")
    List<Video> findByGenderFiltering(GenderType gender, Collection categories);


    @Query(value = "select distinct v from Video v\n" +
            "left join VideoCategory vc\n" +
            "on vc.video.idx = v.idx\n"+
            "where (v.uploader.userInfo.city = :city and v.uploader.userInfo.district = :district)\n" +
            "or vc.category.categoryName in (:categories)\n" +
            "order by v.idx desc")
    List<Video> findByLocationFiltering(String city,String district,Collection categories);

    @Query(value = "select distinct v from Video v\n" +
            "left join VideoCategory vc\n" +
            "on vc.video.idx = v.idx\n"+
            "where vc.category.categoryName in (:categories)\n" +
            "order by v.idx desc")
    List<Video> findByCategoriesFiltering(Collection categories);


}
