package com.mamoori.mamooriback.service;

import java.util.Optional;

import com.mamoori.mamooriback.oauth.User;

public interface UserService {
	Optional<User> findByEmail(String email);
	void create(User user);
}
