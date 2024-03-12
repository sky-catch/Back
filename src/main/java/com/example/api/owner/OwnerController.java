package com.example.api.owner;

import com.example.api.member.MemberDTO;
import com.example.api.owner.dto.CreateOwnerDTO;
import com.example.api.owner.dto.CreateOwnerReq;
import com.example.api.owner.dto.GetOwnerRes;
import com.example.api.owner.dto.Owner;
import com.example.api.owner.dto.UpdateOwnerReq;
import com.example.core.dto.HumanStatus;
import com.example.core.web.security.login.LoginMember;
import com.example.core.web.security.login.LoginOwner;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/owner")
@Tag(name = "사장")
public class OwnerController {

    private final OwnerService ownerService;

    @PostMapping
    @Operation(summary = "사장 생성", description = "사장 생성은 로그인 후 할 수 있습니다. 사업장등록번호의 마지막 숫자는 5여야합니다.")
    @ResponseStatus(HttpStatus.CREATED)
    public void createOwner(@Parameter(hidden = true) @LoginMember MemberDTO memberDTO,
                            @Valid @RequestBody CreateOwnerReq req) {

        CreateOwnerDTO dto = CreateOwnerDTO.builder()
                .name(memberDTO.getName())
                .profileImageUrl(memberDTO.getProfileImageUrl())
                .email(memberDTO.getEmail())
                .platform(memberDTO.getOauthServer())
                .status(HumanStatus.ACTIVE)
                .businessRegistrationNumber(req.getBusinessRegistrationNumber())
                .build();

        ownerService.createOwner(dto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "사장 조회")
    public GetOwnerRes getOwner(@Parameter(hidden = true) @LoginOwner Owner owner,
                                @Parameter(example = "1") @PathVariable(name = "id") long ownerId) {
        return ownerService.getOwner(ownerId);
    }

    @PutMapping(value = "", consumes = {"multipart/form-data"})
    @Operation(summary = "사장 수정")
    public void updateOwner(@ParameterObject @RequestPart UpdateOwnerReq dto,
                            @Parameter(description = "이미지 형식의 파일만 가능") @RequestPart(required = false) MultipartFile file)
            throws IOException {
        ownerService.updateOwner(dto, file);
    }

    /**
     * delete문이 아닌 status 변화
     */
    @PatchMapping("/{id}")
    @Operation(summary = "사장 삭제")
    public void deleteOwner(@PathVariable(name = "id") long ownerId) {
        ownerService.deleteOwner(ownerId);
    }

}