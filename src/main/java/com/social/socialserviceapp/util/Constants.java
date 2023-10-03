package com.social.socialserviceapp.util;

public class Constants {
    public static class RESPONSE_TYPE {

        public static final String SUCCESS = "SUCCESS";
        public static final String ERROR = "ERROR";
        public static final String WARNING = "WARNING";
        public static final String CONFIRM = "CONFIRM";
        public static final String invalidPermision = "invalidPermission";
    }

    public static class RESPONSE_CODE {
        public static final String SUCCESS = "200";
        public static final String BAD_REQUEST = "400";
        public static final String UNAUTHORIZED = "401";
        public static final String FORBIDDEN = "403";

    }

    public static class RESPONSE_MESSAGE {
        public static final String INVALID_OTP = "Invalid otp";
        public static final String OTP_EXPIRED = "Otp expired";
        public static final String RESET_PASSWORD_SUCCESS = "Reset password successfully.";
        public static final String USER_NOT_FOUND = "User not found";

        public static final String MODEL_MAPPER_ERROR = "ModelMapper mapping errors";
    }
}
