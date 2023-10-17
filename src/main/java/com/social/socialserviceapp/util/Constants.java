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
        public static final String ALREADY_FRIEND_SEND_REQUEST = "U guys already friends, so u can't send a friend request again.";
        public static final String ALREADY_FRIEND_ACCEPT_REQUEST = "U guys already friends, so u can't accept a friend request again.";
        public static final String PROFILE_NOT_FOUND = "Profile not found.";
        public static final String SIGNUP_SUCCESSFULLY = "User registered successfully!!!";
        public static final String OTP_VALID_30S = "The OTP is only valid for 60s";
        public static final String TOKEN_VALID_1H = "This token only valid for 1h.";
        public static final String NO_POST = "No posts.";
        public static final String CONTENT_AND_FILE_NULL = "Plz have at least 1 post or 1 image.";
        public static final String POST_NOT_FOUND = "Post not found.";
        public static final String NOT_MINE_POST = "This is not ur post.";
        public static final String INVALID_PAGE_LIMIT = "Page limit must not be less than one.";
        public static final String DELETE_POST_SUCCESSFULLY = "Deleted post successfully.";
        public static final String COMMENT_SUCCESSFULLY = "Comment on a post successfully.";
        public static final String SEND_REQUEST_TO_YOURSELF = "What is wrong with u? U can't send a friend request for urself.";
        public static final String ALREADY_SEND_REQUEST = "U have already sent a friend request.";
    }

    public static class RESPONSE_SCHEMA {
        public static final String OK = "{\n" +
                "  \"timestamp\": \"string\",\n" +
                "  \"type\": \"string\",\n" +
                "  \"status\": 200,\n" +
                "  \"message\": \"string\",\n" +
                "  \"data\": {}\n" +
                "}";
        public static final String BAD_REQUEST = "{\n" +
                "  \"timestamp\": \"string\",\n" +
                "  \"type\": \"string\",\n" +
                "  \"status\": 400,\n" +
                "  \"message\": \"string\",\n" +
                "  \"errors\": [\n" +
                "    {\n" +
                "      \"field\": \"string\",\n" +
                "      \"object\": {},\n" +
                "      \"message\": \"string\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"cause\": \"string\",\n" +
                "  \"path\": \"string\"\n" +
                "}";
    }

}
