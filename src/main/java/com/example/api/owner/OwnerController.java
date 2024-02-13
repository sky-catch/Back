package com.example.api.owner;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/owner")
public class OwnerController {

    private final OwnerService ownerService;

    @PostMapping("")
    public void createOwner(@RequestPart OwnerDTO dto, @RequestPart(required = false) MultipartFile file) throws IOException {
        ownerService.createOwner(dto, file);
    }

    @GetMapping("/{id}")
    public OwnerDTO getOwner(@PathVariable(name = "id") long ownerId) {
        return ownerService.getOwner(ownerId);
    }

    @PutMapping("")
    public void updateOwner(@RequestPart OwnerDTO dto, @RequestPart(required = false) MultipartFile file) throws IOException {
        ownerService.updateOwner(dto, file);
    }

    /**
     * delete문이 아닌 status 변화
     */
    @PatchMapping("/{id}")
    public void deleteOwner(@PathVariable(name = "id") long ownerId) {
        ownerService.deleteOwner(ownerId);
    }

}