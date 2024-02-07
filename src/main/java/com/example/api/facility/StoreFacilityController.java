package com.example.api.facility;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StoreFacilityController {

    private final StoreFacilityService storeFacilityService;

}
