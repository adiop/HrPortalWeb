package com.warn.hr.business;

import com.warn.hr.persistence.entity.hr.staff.common.Staff;
import com.warn.hr.persistence.entity.hr.UserAccount;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class ActiveDirectoryService {

    @Autowired
    private StaffService staffService;

    @Autowired
    private TemporaryService temporaryService;

    @Autowired
    private ActiveDirectoryServiceTemplate activeDirectoryServiceTemplate;

    public UserAccount findByLogin(String login) {
        return activeDirectoryServiceTemplate.findByLogin(login);
    }

    public Boolean pushStaffFromDB2AD(Long id, List<String> fields) {

        try {

            // Get the staff
            Staff staff  = staffService.findById(id);

            // Get userAccount
            UserAccount userAccount = findByLogin(staff.getLogin());

            // Updates the image
            if(fields.contains("image")) {
                String image = staff.getImage().replaceAll("(data:image/jpeg;base64,|data:image/png;base64,|data:image/gif;base64,)", "");
                userAccount.setBase64Image(image);
            }

            // Updates first and last names
            if(fields.contains("name")) {
                userAccount.setFirstName(staff.getPreferredName());
                userAccount.setLastName(staff.getLastName());
            }

            // Updates the email
            if(fields.contains("email")) {
                userAccount.setEmail(staff.getEmail() + "@warn.com");
            }

            // Updates the phone number
            if(fields.contains("phone")) {
                userAccount.setPhone(staff.getPhone());
            }

            // Updates the job title
            if(fields.contains("jobTitle")) {

                UserAccount.JobTitle jobTitle = userAccount.new JobTitle();
                jobTitle.setName(staff.getJobTitle().getName());

                userAccount.setJobTitle(jobTitle);

            }

            // Updates the department
            if(fields.contains("department")) {

                UserAccount.Department department = userAccount.new Department();
                department.setCode(staff.getJobTitle().getDepartment().getDepartmentCode());
                department.setName(staff.getJobTitle().getDepartment().getName());

                userAccount.setDepartment(department);

            }

            // Updates the manager
            if(fields.contains("manager")) {

                UserAccount manager = findByLogin(staffService.findById(staff.getManager().getId()).getLogin());

                userAccount.setManager(manager);

            }

            // Updates the company information
            if("TEMPORARY".equals(userAccount.getType())) {

                String companyName = temporaryService.findById(id).getCompanyName();

                UserAccount.Company company = userAccount.new Company();
                company.setName(companyName);
                company.setDescription(companyName + " Employee");

                userAccount.setCompany(company);

            } else {

                UserAccount.Company company = userAccount.new Company();
                company.setName("Warn Industries");
                company.setDescription("Warn Employee");

                userAccount.setCompany(company);

            }

            activeDirectoryServiceTemplate.update(userAccount);

        } catch (Exception e) {
            log.error("An error has occurred while trying to update the user account", e);
            throw new RuntimeException("An error has occurred while trying to update the user account", e);
        }

        return Boolean.TRUE;

    }

    public Boolean pullStaffFromAD2DB(Long id, List<String> fields) {

        Staff staff = staffService.findById(id);
        UserAccount userAccount = findByLogin(staff.getLogin());

        if(fields.contains("email") && Objects.nonNull(userAccount.getEmail())) {
            staff.setEmail(userAccount.getEmail().toLowerCase().replaceAll("@warn.com", ""));
        }

        if(fields.contains("phone") && Objects.nonNull(userAccount.getPhone())) {
            staff.setPhone(userAccount.getPhone());
        }

        staffService.update(staff);

        return Boolean.TRUE;

    }

    @Component
    @PropertySource("classpath:activedirectory.${spring.profiles.active}.properties")
    public class ActiveDirectoryServiceTemplate {

        @Value("${activedirectory.api.protocol}")
        private String protocol;

        @Value("${activedirectory.api.host}")
        private String host;

        @Value("${activedirectory.api.port}")
        private String port;

        @Value("${activedirectory.api.prefix}")
        private String urlPrefix;

        private RestTemplate restTemplate;

        public ActiveDirectoryServiceTemplate() {
            restTemplate = new RestTemplate();
        }

        public UserAccount findByLogin(String login) {
            String uri = getUri().path("/").path(login).toUriString();
            return restTemplate.exchange(uri, HttpMethod.GET, getHttpEntity(), UserAccount.class).getBody();
        }

        public Boolean update(UserAccount userAccount) {
            String uri = getUri().toUriString();
            return restTemplate.exchange(uri, HttpMethod.PUT, getHttpEntity(userAccount), Boolean.class).getBody();
        }

        private HttpEntity<String> getHttpEntity() {
            return getHttpEntity("");
        }

        private UriComponentsBuilder getUri() {
            return UriComponentsBuilder.newInstance().scheme(protocol).host(host).port(port).path(urlPrefix);
        }

        private <T> HttpEntity<T> getHttpEntity(T body) {
            HttpHeaders headers = new HttpHeaders();
            headers.set("key", "583eccb2-1f67-11e9-ab14-d663bd873d93");
            return new HttpEntity<T>(body, headers);
        }

    }

}

