package com.warn.hr.persistence.entity.hr;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
public @Data class UserAccount {

    private String cn;

    private String dn;

    private String baseDn;

    private String login;

    private String base64Image;

    private String firstName;

    private String lastName;

    private String phone;

    private String email;

    private String type;

    private Department department;

    private JobTitle jobTitle;

    private UserAccount manager;

    private Company company;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Data
    public class Department {

        private String name;

        private String code;

    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Data
    public class JobTitle {

        private String name;

    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Data
    public class Company {

        private String name;

        private String description;

    }

}