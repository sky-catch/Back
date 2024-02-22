package com.example.api.owner.dto;

import com.example.core.dto.BaseDTO;
import com.example.core.dto.HumanStatus;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class GetOwnerRes extends BaseDTO {

    private long ownerId;
    private String imagePath;
    private String name;
    private String phone;
    private String email;
    private HumanStatus status;

}
