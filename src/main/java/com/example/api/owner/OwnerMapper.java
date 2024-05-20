package com.example.api.owner;

import com.example.api.member.UsersMapper;
import com.example.api.owner.dto.Owner;
import com.example.core.dto.HumanStatus;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OwnerMapper extends UsersMapper<Owner> {

    void createOwner(Owner owner);

    Owner getOwner(long ownerId);

    void deleteOwner(long ownerId);

    @Override
    Optional<Owner> findByEmail(String email);

    boolean isExistByEmail(String email);

    // for test
    List<Owner> findAll();
}
