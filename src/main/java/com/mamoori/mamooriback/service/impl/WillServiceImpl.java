package com.mamoori.mamooriback.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mamoori.mamooriback.controller.request.WillRequest;
import com.mamoori.mamooriback.dto.WillDto;
import com.mamoori.mamooriback.entity.Will;
import com.mamoori.mamooriback.oauth.User;
import com.mamoori.mamooriback.repository.WillRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WillServiceImpl  {
	@Autowired
	private WillRepository willRepository;

	@Autowired
	private UserServiceImpl userService;

	public Will create(WillRequest willParameter){
		final User user = userService.read(willParameter.getUserId());
		final Will will = new Will(willParameter.getTitle(), willParameter.getContent(),user);
		return willRepository.save(will);
	}

	public Will read(Long willId) throws Exception {
		// 자바 언어 버전 별 변천사
		// stream - 함수형 프로그래밍 1.  불변성
		// 3. lazy evaluation

		return willRepository.findByWillId(willId).orElseThrow(NullPointerException::new);
	}

}
