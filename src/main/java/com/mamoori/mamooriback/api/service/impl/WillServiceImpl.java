package com.mamoori.mamooriback.api.service.impl;

import com.mamoori.mamooriback.api.dto.PageResponse;
import com.mamoori.mamooriback.api.dto.WillResponse;
import com.mamoori.mamooriback.api.repository.WillRepository;
import com.mamoori.mamooriback.api.service.WillService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class WillServiceImpl implements WillService {

    private final WillRepository willRepository;

    @Override
    public PageResponse<WillResponse> getWillListByEmail(String email, String title, Pageable pageable) {
        Page<WillResponse> search = willRepository.search(email, title, pageable);
        return new PageResponse<>(search.getContent(), (int) search.getTotalElements(), search.getSize(), search.getNumber());
    }
}
