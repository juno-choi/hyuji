package com.juno.normalapi.domain.vo.board;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BoardListVo {
    private int totalPage;
    private Long totalElements;
    private int numberOfElements;
    private Boolean last;
    private Boolean empty;
    private List<BoardVo> list;
}
