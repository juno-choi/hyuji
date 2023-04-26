package com.juno.hyugi.service.rest;

import com.juno.hyugi.domain.entity.rest.RestAreaInfo;
import com.juno.hyugi.domain.vo.rest.RestAreaInfoVo;
import com.juno.hyugi.repository.rest.RestAreaInfoRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;


@SpringBootTest
class RestAreaInfoServiceImplTest {
    @Autowired
    private RestAresInfoService restAresInfoService;

    @Autowired
    private RestAreaInfoRepository restAreaInfoRepository;


    @Test
    @DisplayName("keyword로 휴게소 리스트를 불러오는데 성공")
    void getRestAreaInfoSuccess1(){
        // given
        String restName = "평택휴게소";
        restAreaInfoRepository.save(
                RestAreaInfo.builder()
                        .svarNm(restName)
                        .build()
        );

        // when
        List<RestAreaInfoVo> list = restAresInfoService.getRestAreaInfo("%평택%");

        // then
        Assertions.assertThat(list.stream().filter(
                f -> f.getSvarNm().equals("평택휴게소")
        ).findFirst().get().getSvarNm()).isEqualTo(restName);
    }
}