package com.social.socialserviceapp.controller;

import com.social.socialserviceapp.service.ReportService;
import com.social.socialserviceapp.util.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/report")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "7. Report", description = "The Report API. Nothing more!!!.")
@RequiredArgsConstructor
@ApiResponses(value = {@ApiResponse(responseCode = "200",
        content = {@Content(mediaType = "application/json",
                schema = @Schema(example = Constants.RESPONSE_SCHEMA.OK))}), @ApiResponse(responseCode = "400",
        content = {@Content(mediaType = "application/json",
                schema = @Schema(example = Constants.RESPONSE_SCHEMA.BAD_REQUEST))})})
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Operation(summary = "Export all the past weeks activity of the user to an Excel file.")
    public ResponseEntity<ByteArrayResource> exportExcel() throws IOException {
        return reportService.exportExcel();
    }
}
