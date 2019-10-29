package com.warn.hr.business;

import com.warn.hr.business.rest.ActiveDirectoryRestClient;
import com.warn.hr.persistence.entity.hr.UserAccount;
import com.warn.hr.persistence.entity.hr.staff.common.Staff;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class ActiveDirectoryService {

    @NonNull
    private String activeDirectoryRestServiceKey;

    @NonNull
    private ActiveDirectoryRestClient activeDirectoryRestClient;

    @NonNull
    private StaffService staffService;

    @NonNull
    private TemporaryService temporaryService;



    public UserAccount findByLogin(String login) {
        return activeDirectoryRestClient.findByLogin(activeDirectoryRestServiceKey,
                login);
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

            activeDirectoryRestClient.update(activeDirectoryRestServiceKey,
                    userAccount);

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



}

