package com.social.socialserviceapp.service.impl;

import com.social.socialserviceapp.exception.SocialAppException;
import com.social.socialserviceapp.mapper.UserMapper;
import com.social.socialserviceapp.model.dto.request.UserRequestDTO;
import com.social.socialserviceapp.model.dto.response.UserResponseDTO;
import com.social.socialserviceapp.model.entities.Role;
import com.social.socialserviceapp.model.entities.User;
import com.social.socialserviceapp.model.enums.RoleName;
import com.social.socialserviceapp.repository.RoleRepository;
import com.social.socialserviceapp.repository.UserRepository;
import com.social.socialserviceapp.repository.common.BaseRepository;
import com.social.socialserviceapp.service.UserService;
import com.social.socialserviceapp.service.common.BaseService;
import com.social.socialserviceapp.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl extends BaseService<User, UserRequestDTO> implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    protected BaseRepository<User, Long> getBaseRepository(){
        return userRepository;
    }

    @Override
    public UserResponseDTO register(UserRequestDTO requestDTO) throws SocialAppException, Exception{
        User user = saveOrUpdate(requestDTO);
        return userMapper.convertUserToUserResponseDTO(user);
    }

    @Override
    protected void validate(UserRequestDTO requestDTO) throws SocialAppException{
        List<User> userList = userRepository.findConflictByEmail(requestDTO.getEmail());
        if (!userList.isEmpty()) {
            throw new SocialAppException(Constants.RESPONSE_CODE.SUCCESS, "Error: Email is already in use!!!");
        }
    }

    @Override
    protected User customBeforeSave(UserRequestDTO requestDTO){
        User user = userMapper.convertRequestDTOToUser(requestDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role role = roleRepository.findByRoleName(RoleName.USER)
                .orElse(null);
        HashSet<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);
        return user;
    }

}
