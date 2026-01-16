package com.example.OAuth2InSpringBoot.Repository;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.OAuth2InSpringBoot.Entity.LoginHistory;

@Repository
public interface LoginHistoryRepository extends JpaRepository<LoginHistory, Long>{
	
	List<LoginHistory> findByEmailOrderByLoginTimeDesc(String email);

}
