package com.social.socialserviceapp.service;

import com.social.socialserviceapp.model.dto.request.UpdateInformationRequestDTO;
import com.social.socialserviceapp.result.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface ProfileService {

    public Response updateInformation(UpdateInformationRequestDTO requestDTO);

    public ResponseEntity<?> showAvatar() throws IOException;

    public Response showMyInfo();

    public Response showOtherInfo(Long userId);

    public Response uploadAvatar(MultipartFile multipartFile) throws IOException;

}
