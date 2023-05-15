package com.juno.hyugi.repository.rest;

import com.juno.hyugi.domain.entity.rest.RestAreaInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestAreaInfoRepository extends JpaRepository<RestAreaInfo, Long> {
    List<RestAreaInfo> findBySvarNmLike(String keyword);
}
