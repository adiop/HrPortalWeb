package com.warn.hr.web.common;

import java.util.HashSet;
import java.util.Set;

public class Constants {

    public static class Security {

        public static final String TOKEN_PARAM = "auth-token";

        public static final String SESSION_AUTH_INFO = "session-auth";

        public static final String ACCESS_DENIED_PATH = "/error/error_403.html";

        public static final String ACCESS_ROLE = "HR Portal";

        public static final String ADMIN_ROLE = "HR Portal Admins";

        public static final Set<String> ALLOWED_PATHS = new HashSet<>();

        public static final Set<String> ALLOWED_ROLES = new HashSet<>();

        static {

            ALLOWED_PATHS.add("error");
            ALLOWED_PATHS.add("webjars");
            ALLOWED_PATHS.add("resources/images");

            ALLOWED_ROLES.add(ACCESS_ROLE);
            ALLOWED_ROLES.add(ADMIN_ROLE);

        }

    }

}
