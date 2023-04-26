package com.juno.hyugi.service.rest;

import com.juno.hyugi.domain.vo.rest.RestAreaInfoVo;

import java.util.List;

public interface RestAresInfoService {
    List<RestAreaInfoVo> getRestAreaInfo(String keyword);
}
