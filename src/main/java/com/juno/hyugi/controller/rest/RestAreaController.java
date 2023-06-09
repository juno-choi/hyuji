package com.juno.hyugi.controller.rest;

import com.juno.hyugi.api.Response;
import com.juno.hyugi.api.ResponseCode;
import com.juno.hyugi.domain.vo.rest.RestAreaInfoListVo;
import com.juno.hyugi.service.rest.RestAresInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/rest")
@RequiredArgsConstructor
public class RestAreaController {
    private final RestAresInfoService restAresInfoService;

    @GetMapping("/search")
    public ResponseEntity<Response<RestAreaInfoListVo>> search(@RequestParam(name = "keyword") String keyword){
        return ResponseEntity.ok(Response.<RestAreaInfoListVo>builder()
                .code(ResponseCode.SUCCESS)
                .message("성공")
                .data(restAresInfoService.getRestAreaInfo(keyword))
                .build());
    }
}
