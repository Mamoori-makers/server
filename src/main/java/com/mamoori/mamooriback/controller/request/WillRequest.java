package com.mamoori.mamooriback.controller.request;

import org.springframework.web.bind.annotation.RestController;

import com.mamoori.mamooriback.service.WillService;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
public class WillRequest {
	private String title;
	private String content;
	private Long userId;

	public WillRequest(String title, String content, Long userId) {
		this.title = title;
		this.content = content;
		this.userId = userId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
}
