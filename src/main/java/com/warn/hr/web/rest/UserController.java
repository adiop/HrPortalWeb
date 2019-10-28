package com.warn.hr.web.rest;

import com.warn.hr.persistence.entity.common.AbstractEntity;
import com.warn.hr.persistence.entity.hr.UserAccount;
import com.warn.hr.persistence.entity.hr.staff.common.Staff;
import com.warn.hr.web.rest.common.AbstractController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
public class UserController extends AbstractController {

    @GetMapping("/hr/api/v1/user/")
    public @ResponseBody ResponseEntity<UserAccount> getUserFromRequest(HttpServletRequest request) {
        return find(() -> getUser(request));
    }

    @GetMapping("/hr/api/v1/user/staff")
    public @ResponseBody ResponseEntity<Staff> getStaffFromRequest(HttpServletRequest request) {
        return find(() -> getStaff(request));
    }

    @GetMapping("/hr/api/v1/user/role/admin")
    public @ResponseBody ResponseEntity<SimpleBooleanValue> isAdmin(HttpServletRequest request) {
        return find(() -> SimpleBooleanValue.of(isUserAdmin(request)));
    }

    @GetMapping("/hr/api/v1/user/role/{role}")
    public @ResponseBody ResponseEntity<AbstractEntity> isUserInRole(@PathVariable("role") String role, HttpServletRequest request) {
        return find(() -> new AbstractEntity() {
            public Boolean getIsInRole() {
                return isUserInRole(request, role);
            }
        });
    }

}
