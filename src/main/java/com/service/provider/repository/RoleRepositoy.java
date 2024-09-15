package com.service.provider.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.service.provider.model.Role;

public interface RoleRepositoy extends JpaRepository<Role, Long>{
    Role findByRoleName(String roleName);
}
