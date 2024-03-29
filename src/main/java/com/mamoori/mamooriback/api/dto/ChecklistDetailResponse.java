package com.mamoori.mamooriback.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ChecklistDetailResponse {
    private Long id;
    private Long checkedTaskCount;
    private Long totalTaskCount;
    private Integer progress;

    private Long prevId;
    private Long nextId;
    private List<ChecklistDto> checklist;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;
}
