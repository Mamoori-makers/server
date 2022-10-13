package com.mamoori.mamooriback.dto;

import com.mamoori.mamooriback.entity.Will;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WillDto {
	private Long willId;
	private String title;
	private String content;
	private String receiver;

	public WillDto(Will will) {
		this.willId = will.getWillId();
		this.title = will.getTitle();
		this.content = will.getContent();
		this.receiver= will.getReceiver();
	}
}
