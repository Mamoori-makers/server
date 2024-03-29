package com.mamoori.mamooriback.api.service.impl;

import com.mamoori.mamooriback.api.dto.*;
import com.mamoori.mamooriback.api.entity.Checklist;
import com.mamoori.mamooriback.api.entity.User;
import com.mamoori.mamooriback.api.entity.UserChecklist;
import com.mamoori.mamooriback.api.entity.UserChecklistAnswer;
import com.mamoori.mamooriback.api.repository.ChecklistRepository;
import com.mamoori.mamooriback.api.repository.UserChecklistAnswerRepository;
import com.mamoori.mamooriback.api.repository.UserChecklistRepository;
import com.mamoori.mamooriback.api.repository.UserChecklistRepository.ChecklistPrevAndNext;
import com.mamoori.mamooriback.api.repository.UserRepository;
import com.mamoori.mamooriback.api.service.ChecklistService;
import com.mamoori.mamooriback.exception.BusinessException;
import com.mamoori.mamooriback.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChecklistServiceImpl implements ChecklistService {
    private final ChecklistRepository checklistRepository;
    private final UserChecklistRepository userChecklistRepository;
    private final UserChecklistAnswerRepository userChecklistAnswerRepository;
    private final UserRepository userRepository;

    @Override
    public List<ChecklistTaskResponse> getChecklistTasks() {
        return checklistRepository.getChecklistTasks();
    }

    @Override
    public ChecklistPageResponse getChecklists(String email, Pageable pageable) {
        List<ChecklistResponse> checklists = new ArrayList<>();
        Page<UserChecklist> checklistPage = userChecklistRepository.getChecklistPage(email, pageable);
        checklistPage.getTotalPages();
        for (UserChecklist userChecklist : checklistPage.getContent()) {
            Long totalTaskCount = userChecklistRepository.getTotalTaskCount(userChecklist.getUserChecklistId());
            Long checkedTaskCount = userChecklistRepository.getCheckedTaskCount(userChecklist.getUserChecklistId());
            Integer progress = roundRatioToFirstDigit(checkedTaskCount, totalTaskCount);
            List<ChecklistDto> dto = userChecklistRepository.getChecklist(userChecklist.getUserChecklistId());
            checklists.add(
                    ChecklistResponse.builder()
                            .id(userChecklist.getUserChecklistId())
                            .totalTaskCount(totalTaskCount)
                            .checkedTaskCount(checkedTaskCount)
                            .progress(progress)
                            .checklist(dto)
                            .createdAt(userChecklist.getCreateAt())
                    .build()
            );
        }
        return new ChecklistPageResponse(
                checklistPage.getTotalElements(),
                checklistPage.getNumber() + 1,
                checklistPage.getSize(),
                checklistPage.getTotalPages(),
                checklistPage.isFirst(),
                checklistPage.isLast(),
                userChecklistRepository.findLastChecklistAnswerByEmail(email),
                checklists
        );
    }

    @Override
    public ChecklistDetailResponse getChecklistByEmailAndUserChecklistId(String email, Long userChecklistId) {
        UserChecklist userChecklist = userChecklistRepository.findById(userChecklistId)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.INVALID_REQUEST, ErrorCode.INVALID_REQUEST.getMessage()
                ));

        if (!email.equals(userChecklist.getUser().getEmail())){
            throw new BusinessException(ErrorCode.FORBIDDEN, ErrorCode.FORBIDDEN.getMessage());
        }

        ChecklistPrevAndNext prevAndNext = userChecklistRepository.findPrevAndNextById(userChecklist.getUser().getUserId(), userChecklistId)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.INVALID_REQUEST, ErrorCode.INVALID_REQUEST.getMessage()
                ));

        Long totalTaskCount = userChecklistRepository.getTotalTaskCount(userChecklistId);
        Long checkedTaskCount = userChecklistRepository.getCheckedTaskCount(userChecklistId);
        Integer progress = roundRatioToFirstDigit(checkedTaskCount, totalTaskCount);

        List<ChecklistDto> checklist = userChecklistRepository.getChecklist(userChecklistId);

        return ChecklistDetailResponse.builder()
                .id(userChecklist.getUserChecklistId())
                .totalTaskCount(totalTaskCount)
                .checkedTaskCount(checkedTaskCount)
                .progress(progress)
                .createdAt(userChecklist.getCreateAt())
                .checklist(checklist)
                .prevId(prevAndNext.getPrevId())
                .nextId(prevAndNext.getNextId())
                .build();
    }

    @Transactional
    @Override
    public void createChecklist(String email, List<ChecklistRequest> checklistRequests) {
        log.debug("createChecklist called...");
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.FORBIDDEN, ErrorCode.FORBIDDEN.getMessage()));
        log.debug("user : {}", user.getEmail());
        LocalDateTime lastChecklistDateTime = userChecklistRepository.findLastChecklistAnswerByEmail(email);
        log.debug("lastChecklistDateTime : {}", lastChecklistDateTime);

        if (lastChecklistDateTime != null && isTodayDate(lastChecklistDateTime)) {
            throw new BusinessException(ErrorCode.CHECKLIST_ALREADY_EXISTS_FOR_TODAY, ErrorCode.CHECKLIST_ALREADY_EXISTS_FOR_TODAY.getMessage());
        }

        UserChecklist saveUserChecklist = userChecklistRepository.save(new UserChecklist(user));
        log.debug("createChecklist -> userChecklistId : {}", saveUserChecklist.getUserChecklistId());

        // 전체 체크리스트 항목 가져오기
        List<Long> taskIds = getChecklistTasks().stream().map(task -> task.getId()).collect(Collectors.toList());

        // checklistRequests에 isChecked가 false인 값 추가
        for (Long taskId : taskIds) {
            if (!contains(checklistRequests, taskId)) {
                checklistRequests.add(new ChecklistRequest(taskId, false));
            }
        }

        for (ChecklistRequest checklistRequest : checklistRequests) {
            Checklist findChecklist = checklistRepository.findById(checklistRequest.getId()).get();
            log.debug("createChecklist -> findChecklist : {}", findChecklist.getChecklistId());
            UserChecklistAnswer saveAnswer = userChecklistAnswerRepository.save(checklistRequest.toEntity(saveUserChecklist, findChecklist));
            log.debug("createChecklist -> saveAnswer : {}", saveAnswer.getAnswerId());
        }
    }

    @Override
    public void deleteUserChecklist(String email, Long userChecklistId) {
        UserChecklist userChecklist = userChecklistRepository.findByUser_EmailAndUserChecklistId(email, userChecklistId)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.FORBIDDEN, ErrorCode.FORBIDDEN.getMessage()
                ));
        userChecklistRepository.delete(userChecklist);
    }

    @Override
    public ChecklistTodayResponse getChecklistToday(String email) {
        LocalDateTime lastChecklistDateTime = userChecklistRepository.findLastChecklistAnswerByEmail(email);
        log.debug("lastChecklistDateTime : {}", lastChecklistDateTime);

        boolean result = false;

        if (lastChecklistDateTime != null && isTodayDate(lastChecklistDateTime)) {
            result = true;
        }

        return new ChecklistTodayResponse(result);
    }

    private boolean isTodayDate(LocalDateTime localDateTime) {
        if (localDateTime.toLocalDate().isEqual(LocalDate.now())) {
            return true;
        } else {
            return false;
        }
    }

    private boolean contains(List<ChecklistRequest> requests, Long taskId) {
        for (ChecklistRequest checklistRequest : requests) {
            if (taskId == checklistRequest.getId()) {
                return true;
            }
        }
        return false;
    }

    private int roundRatioToFirstDigit(long num1, long num2) {
        return (int) Math.round(num1 * 1000 / num2 / 10.0);
    }
}
