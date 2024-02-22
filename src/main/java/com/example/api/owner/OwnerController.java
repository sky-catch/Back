package com.example.api.owner;

import com.example.api.owner.dto.CreateOwnerReq;
import com.example.api.owner.dto.GetOwnerRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/owner")
@Tag(name = "사장")
public class OwnerController {

    private final OwnerService ownerService;

    @PostMapping(value = "", consumes = {"multipart/form-data"})
    @Operation(summary = "사장 생성")
    public void createOwner(@ParameterObject @RequestPart CreateOwnerReq dto,
                            @Parameter(description = "이미지 형식의 파일만 가능") @RequestPart(required = false) MultipartFile file) throws IOException {
        ownerService.createOwner(dto, file);
    }

    @GetMapping("/{id}")
    @Operation(summary = "사장 조회")
    public GetOwnerRes getOwner(@Parameter(example = "1") @PathVariable(name = "id") long ownerId) {
        return ownerService.getOwner(ownerId);
    }

    @PutMapping(value = "", consumes = {"multipart/form-data"})
    @Operation(summary = "사장 수정")
    public void updateOwner(@ParameterObject @RequestPart CreateOwnerReq createOwnerReq,
                            @Parameter(description = "이미지 형식의 파일만 가능") @RequestPart(required = false) MultipartFile file) throws IOException {
        ownerService.updateOwner(createOwnerReq, file);
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