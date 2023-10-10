package com.social.socialserviceapp.controller;

import com.social.socialserviceapp.model.dto.request.UpdateInformationRequestDTO;
import com.social.socialserviceapp.result.Response;
import com.social.socialserviceapp.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/profile")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Profile", description = "The Profile API. Nothing more!!!.")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @PostMapping(value = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Operation(description = "aduchat")
    public Response updateInformation(UpdateInformationRequestDTO requestDTO,
                                      @RequestPart(value = "avatar", required = false) MultipartFile multipartFile) throws IOException {
        return profileService.updateInformation(requestDTO, multipartFile);
    }

    @GetMapping(value = "/avatar")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Operation(description = "aduchat")
    public ResponseEntity<?> showAvatar() throws IOException {
        return profileService.showAvatar();
    }

    @GetMapping("/")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public Response myInfo() {
        return profileService.showMyInfo();
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public Response showOtherInfo(@PathVariable Long userId) {
        return profileService.showOtherInfo(userId);
    }

}
