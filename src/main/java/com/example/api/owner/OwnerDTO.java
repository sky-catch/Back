package com.example.api.owner;

import com.example.core.dto.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OwnerDTO extends BaseDTO {

    private long ownerId;
    private String name;
    private String imagePath;
    private String imageFileName;
    private String phone;
    private String email;
    private String platform;
    private String status;
}