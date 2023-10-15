package com.social.socialserviceapp.controller;

import com.social.socialserviceapp.service.ReportService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReportControllerTest {

    @InjectMocks
    private ReportController reportController;
    @Mock
    private ReportService reportService;

    @BeforeEach
    void setUp(){
    }

    @AfterEach
    void tearDown(){
    }

    @Test
    void exportExcel() throws IOException{
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(new MediaType("application", "force-download"));
        httpHeaders.set(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=report" + new SimpleDateFormat("yyyyMMdd").format(
                        new Date()) + "_" + "username" + ".xlsx");
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        when(reportService.exportExcel()).thenReturn(
                new ResponseEntity<>(new ByteArrayResource(stream.toByteArray()), httpHeaders, HttpStatus.CREATED));
        ResponseEntity response = reportController.exportExcel();
        assertThat(response).isNotNull();
        assertEquals(201, response.getStatusCodeValue());
    }
}