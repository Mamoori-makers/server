package com.mamoori.mamooriback.service;

import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mamoori.mamooriback.controller.request.WillRequest;
import com.mamoori.mamooriback.entity.Will;

public interface WillService {
	Page<Will> getWillList(Map<String,Object> filter, Pageable pageable);
	Will read(Long willId);
}
