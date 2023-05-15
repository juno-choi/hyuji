package com.juno.hyugi.service.rest;

import com.juno.hyugi.domain.vo.rest.RestAreaInfoListVo;


public interface RestAresInfoService {
    RestAreaInfoListVo getRestAreaInfo(String keyword);
}
