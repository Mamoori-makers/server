package com.mamoori.mamooriback.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mamoori.mamooriback.controller.request.UserRequest;
import com.mamoori.mamooriback.oauth.User;
import com.mamoori.mamooriback.oauth.UserRepository;
import com.mamoori.mamooriback.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	private final List<User> users = new ArrayList<>();

	// create 없이 delete update read 만 있어도 됨
	public User create(UserRequest userParameter) {
		final User user = new User(userParameter.getUserId(),userParameter.getSocialId(), userParameter.getEmail(),userParameter.getNickName(),userParameter.getPicture(),userParameter.getRole());
		return userRepository.save(user);
	}

	@Override
	@Transactional
	public void create(User user) {
		userRepository.save(user);
	}

	@Cacheable(cacheNames = "user", key = "#id")
	public User read(Long id) {
		return userRepository.findById(id).orElseThrow(NullPointerException::new);
	}

	@Override
	public Optional<User> findByEmail(String email) {
		return userRepository.findByEmail(email);
	}
}