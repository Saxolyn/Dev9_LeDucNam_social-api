package com.social.socialserviceapp.service.impl;

import com.social.socialserviceapp.model.enums.ReactStatus;
import com.social.socialserviceapp.repository.CommentRepository;
import com.social.socialserviceapp.repository.FriendRepository;
import com.social.socialserviceapp.repository.PostRepository;
import com.social.socialserviceapp.repository.ReactRepository;
import com.social.socialserviceapp.service.PostService;
import com.social.socialserviceapp.service.ReportService;
import com.social.socialserviceapp.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Transactional
@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final PostRepository postRepository;
    private final FriendRepository friendRepository;
    private final CommentRepository commentRepository;
    private final ReactRepository reactRepository;

    @Override
    public ResponseEntity<ByteArrayResource> exportExcel() throws IOException {
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        HttpHeaders httpHeaders = new HttpHeaders();
        try (XSSFWorkbook workbook = createAWorkBook(username)) {
            httpHeaders.setContentType(new MediaType("application", "force-download"));
            httpHeaders.set(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=report" + new SimpleDateFormat("yyyyMMdd").format(
                            new Date()) + "_" + username + ".xlsx");
            workbook.write(stream);
        }
        return new ResponseEntity<>(new ByteArrayResource(stream.toByteArray()), httpHeaders, HttpStatus.CREATED);
    }

    public XSSFWorkbook createAWorkBook(String username) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Report");
        XSSFRow header = sheet.createRow(0);
        header.createCell(0)
                .setCellValue("Number of posts last week");
        header.createCell(1)
                .setCellValue("Number of new friend last week");
        header.createCell(2)
                .setCellValue("Number of likes last week");
        header.createCell(3)
                .setCellValue("Number of comments last week");
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
        sheet.autoSizeColumn(3);
        Row rowData = sheet.createRow(1);
        rowData.createCell(0)
                .setCellValue(postRepository.countPostsLastWeekByCreatedBy(username));
        rowData.createCell(1)
                .setCellValue(friendRepository.countFriendsLastWeekByCreatedBy(username));
        rowData.createCell(2)
                .setCellValue(
                        reactRepository.countReactsLastWeekByCreatedByAndStatus(username, ReactStatus.LIKE.ordinal()));
        rowData.createCell(3)
                .setCellValue(commentRepository.countCommentsLastWeekByCreatedBy(username));
        return workbook;
    }
}
