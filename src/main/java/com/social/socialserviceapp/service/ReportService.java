package com.social.socialserviceapp.service;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface ReportService {

    ResponseEntity<ByteArrayResource> exportExcel() throws IOException;

}
