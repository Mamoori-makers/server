package com.mamoori.mamooriback.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mamoori.mamooriback.controller.request.WillRequest;
import com.mamoori.mamooriback.dto.WillDto;
import com.mamoori.mamooriback.entity.Will;
import com.mamoori.mamooriback.service.impl.WillServiceImpl;

@RestController
@RequestMapping(value = "/will")
public class WillController {
	@Autowired
	private WillServiceImpl willServiceImpl;

	@PostMapping()
	public WillDto createWill(@RequestBody WillRequest willRequest) {
		final Will will = willServiceImpl.create(willRequest);
		return new WillDto(will);
	}

	@GetMapping("/{willId}")
	public WillDto readWill(@PathVariable(name = "willId") Long willId) throws Exception {
		final Will will =  willServiceImpl.read(willId);
		return new WillDto(will);
	}



	}



