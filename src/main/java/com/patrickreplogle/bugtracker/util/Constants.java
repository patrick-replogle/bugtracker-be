package com.patrickreplogle.bugtracker.util;

public final class Constants {
    public static final String ADMIN_ROLE = "ADMIN";
    public static final String USER_ROLE = "USER";
    public static final String OAUTHCLIENTID = "OAUTHCLIENTID";
    public static final String OAUTHCLIENTSECRET = "OAUTHCLIENTSECRET";
    public static final String RUNTIME_ENV = "RUNTIME_ENV";
    public static final String POSTGRES_USER = "POSTGRES_USER";
    public static final String POSTGRES_PASSWORD = "POSTGRES_PASSWORD";
    public static final String POSTGRES_URL = "POSTGRES_URL";
    public static final String CLOUDINARY_NAME = "CLOUDINARY_NAME";
    public static final String CLOUDINARY_API_KEY = "CLOUDINARY_API_KEY";
    public static final String CLOUDINARY_API_SECRET = "CLOUDINARY_API_SECRET";

    public static final String[] requiredEnvVars = {
            OAUTHCLIENTID, OAUTHCLIENTSECRET, RUNTIME_ENV, POSTGRES_USER,
            POSTGRES_PASSWORD, POSTGRES_URL, CLOUDINARY_NAME, CLOUDINARY_API_KEY,
            CLOUDINARY_API_SECRET
    };
}
