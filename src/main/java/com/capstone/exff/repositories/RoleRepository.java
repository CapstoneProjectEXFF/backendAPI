package com.capstone.exff.repositories;

import com.capstone.exff.entities.RoleEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RoleRepository extends CrudRepository<RoleEntity, Integer> {
    @Override
    List<RoleEntity> findAll();

    RoleEntity findFirstById(int id);

    RoleEntity findTop1ByName(String name);
}
