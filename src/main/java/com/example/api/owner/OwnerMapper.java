package com.example.api.owner;

import com.example.api.member.UsersMapper;
import com.example.api.owner.dto.Owner;
import com.example.core.dto.HumanStatus;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OwnerMapper extends UsersMapper<Owner> {

    void createOwner(Owner dto);

    Owner getOwner(long ownerId);

    void updateOwner(Owner dto);

    void deleteOwner(long ownerId, HumanStatus status);

    @Override
    Optional<Owner> findByEmail(String email);

    boolean isExistByEmail(String email);

    List<Owner> findAll();

    void deleteAll();
}
