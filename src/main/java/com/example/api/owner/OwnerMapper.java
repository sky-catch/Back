package com.example.api.owner;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OwnerMapper {

    void createOwner(OwnerDTO dto);

    OwnerDTO getOwner(long ownerId);

    void updateOwner(OwnerDTO dto);

    void deleteOwner(OwnerDTO dto);
}
