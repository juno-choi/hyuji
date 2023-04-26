package com.juno.hyugi.service.rest;

import com.juno.hyugi.domain.entity.rest.RestAreaInfo;
import com.juno.hyugi.domain.vo.rest.RestAreaInfoVo;
import com.juno.hyugi.repository.rest.RestAreaInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RestAreaInfoServiceImpl implements RestAresInfoService{
    private final RestAreaInfoRepository restAreaInfoRepository;

    @Override
    public List<RestAreaInfoVo> getRestAreaInfo(String keyword) {
        log.info("keyword = {}", keyword);
        List<RestAreaInfo> bySvarNmLike = restAreaInfoRepository.findBySvarNmLike(keyword);
        List<RestAreaInfoVo> list = new ArrayList<>();
        for(RestAreaInfo i : bySvarNmLike){
            list.add(
                    RestAreaInfoVo.builder()
                            .id(i.getId())
                            .bsopAdtnlFcltCd(i.getBsopAdtnlFcltCd())
                            .cocrPrkgTrcn(i.getCocrPrkgTrcn())
                            .dspnPrkgTrcn(i.getDspnPrkgTrcn())
                            .fscarPrkgTrcn(i.getFscarPrkgTrcn())
                            .gudClssCd(i.getGudClssCd())
                            .gudClssNm(i.getGudClssNm())
                            .hdqrCd(i.getHdqrCd())
                            .hdqrNm(i.getHdqrNm())
                            .mtnofCd(i.getMtnofCd())
                            .mtnofNm(i.getMtnofNm())
                            .pstnoCd(i.getPstnoCd())
                            .routeCd(i.getRouteCd())
                            .routeNm(i.getRouteNm())
                            .rprsTelNo(i.getRprsTelNo())
                            .svarAddr(i.getSvarAddr())
                            .svarCd(i.getSvarCd())
                            .svarGsstClssCd(i.getSvarGsstClssCd())
                            .svarNm(i.getSvarNm())
                            .svarGsstClssNm(i.getSvarGsstClssNm())
                            .build()
            );
        }
        return list;
    }
}
