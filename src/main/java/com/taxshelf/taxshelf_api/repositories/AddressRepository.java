package com.taxshelf.taxshelf_api.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.taxshelf.taxshelf_api.entities.Address;

@Repository
public interface AddressRepository extends CrudRepository<Address, String>{

}
