package com.taxshelf.taxshelf_api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.taxshelf.taxshelf_api.entities.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, String>{
    Optional<Role> findByName(String name);
    boolean existsByName(String name);
}
