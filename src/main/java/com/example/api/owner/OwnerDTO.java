package com.example.api.owner;

import com.example.core.dto.BaseDTO;
import com.example.core.dto.HumanStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class OwnerDTO extends BaseDTO {

    private long ownerId;
    private String name;
    private String imagePath;
    private String phone;
    private String email;
    private String platform;
    private HumanStatus status;

}