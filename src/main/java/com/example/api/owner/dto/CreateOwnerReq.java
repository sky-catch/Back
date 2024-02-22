package com.example.api.owner.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOwnerReq {

    private String name;
    private String phone;
    private String email;

}
