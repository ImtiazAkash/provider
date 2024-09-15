package com.service.provider.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.service.provider.model.User;

public interface UserRepository extends JpaRepository<User, Long>{

    User findByEmail(String email);
}
