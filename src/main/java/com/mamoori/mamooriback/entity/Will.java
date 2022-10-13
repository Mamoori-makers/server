package com.mamoori.mamooriback.entity;

import javax.persistence.*;

import com.mamoori.mamooriback.oauth.User;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Will extends BaseTimeEntity{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "will_id")
	private Long willId;

	@Column
	private String title;

	@Column
	private String content;

	@Column
	private String receiver;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="user_id")
	private User user;


	public Will() {

	}

	public Will(Long willId, String title, String content, String receiver, User user) {
		this.willId = willId;
		this.title = title;
		this.content = content;
		this.receiver = receiver;
		this.user = user;
	}

	public Will(String title, String content, User user) {
		this.title = title;
		this.content = content;
		this.user = user;
	}
}
