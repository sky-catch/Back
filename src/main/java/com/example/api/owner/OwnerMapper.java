package com.example.api.owner;

import com.example.api.owner.dto.Owner;
import com.example.core.dto.HumanStatus;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OwnerMapper {

    void createOwner(Owner dto);

    Owner getOwner(long ownerId);

    void updateOwner(Owner dto);

    void deleteOwner(long ownerId, HumanStatus status);
}
