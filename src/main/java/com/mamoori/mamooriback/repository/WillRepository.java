package com.mamoori.mamooriback.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.mamoori.mamooriback.dto.WillDto;
import com.mamoori.mamooriback.entity.Will;

public interface WillRepository extends JpaRepository<Will,Long> {
	Optional<Will> findByWillId(Long willId);
}

