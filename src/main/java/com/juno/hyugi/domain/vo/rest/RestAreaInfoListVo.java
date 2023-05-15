package com.juno.hyugi.domain.vo.rest;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class RestAreaInfoListVo {
    List<RestAreaInfoVo> list;
}
