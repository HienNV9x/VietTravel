package com.viettravelbk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.viettravelbk.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long>{
	Role findByName(String name);
}
