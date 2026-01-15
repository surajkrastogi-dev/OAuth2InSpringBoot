package com.example.SpringBootWithOAuth2.Repository;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.SpringBootWithOAuth2.Entity.LoginHistory;

@Repository
public interface LoginHistoryRepository extends JpaRepository<LoginHistory, Long>{
	
	List<LoginHistory> findByEmailOrderByLoginTimeDesc(String email);

}
