package com.mamoori.mamooriback.api.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;

@RequiredArgsConstructor
@Getter
public class ReponseEntity<T> extends HttpEntity<T> {
}
