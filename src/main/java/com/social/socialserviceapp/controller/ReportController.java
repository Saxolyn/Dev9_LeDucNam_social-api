package com.social.socialserviceapp.controller;

import com.social.socialserviceapp.service.ReportService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/report")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Report", description = "The Report API. Nothing more!!!.")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/")
    public ResponseEntity<ByteArrayResource> exportExcel() throws IOException {
        return reportService.exportExcel();
    }
}
