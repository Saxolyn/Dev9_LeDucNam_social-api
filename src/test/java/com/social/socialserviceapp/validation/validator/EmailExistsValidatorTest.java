package com.social.socialserviceapp.validation.validator;

import com.social.socialserviceapp.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ClockProvider;
import javax.validation.ConstraintValidatorContext;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmailExistsValidatorTest {

    @InjectMocks
    private EmailExistsValidator emailExistsValidator;
    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp(){
    }

    @AfterEach
    void tearDown(){
    }

    @Test
    void initialize(){
        emailExistsValidator.initialize(null);
    }

    @Test
    void isValid(){
        when(userRepository.existsUserByEmail(any())).thenReturn(true);
        boolean isValid = emailExistsValidator.isValid("aduchar@gmail.com", new ConstraintValidatorContext() {
            @Override
            public void disableDefaultConstraintViolation(){

            }

            @Override
            public String getDefaultConstraintMessageTemplate(){
                return null;
            }

            @Override
            public ClockProvider getClockProvider(){
                return null;
            }

            @Override
            public ConstraintViolationBuilder buildConstraintViolationWithTemplate(String messageTemplate){
                return null;
            }

            @Override
            public <T> T unwrap(Class<T> type){
                return null;
            }
        });
        assertTrue(!isValid);
    }

    @Test
    void isNotValid(){
        when(userRepository.existsUserByEmail(any())).thenReturn(false);
        boolean isValid = emailExistsValidator.isValid("aduchar@gmail.com", new ConstraintValidatorContext() {
            @Override
            public void disableDefaultConstraintViolation(){

            }

            @Override
            public String getDefaultConstraintMessageTemplate(){
                return null;
            }

            @Override
            public ClockProvider getClockProvider(){
                return null;
            }

            @Override
            public ConstraintViolationBuilder buildConstraintViolationWithTemplate(String messageTemplate){
                return null;
            }

            @Override
            public <T> T unwrap(Class<T> type){
                return null;
            }
        });
        assertTrue(isValid);
    }
}