package com.taxshelf.taxshelf_api.controllers;

import java.net.URI;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.taxshelf.taxshelf_api.dtos.RoleDto;
import com.taxshelf.taxshelf_api.repositories.RoleRepository;
import com.taxshelf.taxshelf_api.entities.*;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
public class RoleController {

    private final RoleRepository roleRepository;

    @PostMapping
    public ResponseEntity<?> createRole(@RequestBody RoleDto role, UriComponentsBuilder uriBuilder){
        if(roleRepository.existsByName(role.getName())){
            return ResponseEntity.badRequest().body(Map.of("error", "Role name already exists"));
        }

        var newRole =  Role.builder().name(role.getName()).build();

        Role savedRole = roleRepository.save(newRole);
        URI location = uriBuilder.path("role/{id}").buildAndExpand(savedRole.getId()).toUri();
        return ResponseEntity.created(location).body(savedRole);
    }

    @GetMapping
    public List<Role> getAllRoles(){
        return roleRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Role> getRole(@PathVariable String id){
        return roleRepository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRole(@PathVariable String id, @RequestBody RoleDto role){
        return roleRepository.findById(id).map(existing -> {
            existing.setName(role.getName());
            roleRepository.save(existing);
            return ResponseEntity.ok(existing);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRole(@PathVariable String id){
        return roleRepository.findById(id).map(existing -> {
            roleRepository.delete(existing);
            return ResponseEntity.noContent().build();
        }).orElse(ResponseEntity.notFound().build());
    }
}
