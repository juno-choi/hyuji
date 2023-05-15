package com.juno.hyugi.service.rest;

import com.juno.hyugi.domain.entity.rest.RestAreaInfo;
import com.juno.hyugi.domain.vo.rest.RestAreaInfoListVo;
import com.juno.hyugi.domain.vo.rest.RestAreaInfoVo;
import com.juno.hyugi.repository.rest.RestAreaInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class RestAreaInfoServiceImpl implements RestAresInfoService{
    private final RestAreaInfoRepository restAreaInfoRepository;

    @Override
    public RestAreaInfoListVo getRestAreaInfo(String keyword) {
        log.info("keyword = {}", keyword);
        StringBuilder sb = new StringBuilder();
        sb.append("%");
        sb.append(keyword);
        sb.append("%");

        List<RestAreaInfo> bySvarNmLike = restAreaInfoRepository.findBySvarNmLike(sb.toString());
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
        return RestAreaInfoListVo.builder()
                .list(list)
                .build();
    }
}
