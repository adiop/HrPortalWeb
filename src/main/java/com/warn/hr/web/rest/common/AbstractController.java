package com.warn.hr.web.rest.common;

import com.warn.hr.business.StaffService;
import com.warn.hr.persistence.entity.hr.staff.common.Staff;
import com.warn.hr.persistence.entity.hr.User;
import javaslang.control.Try;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;

@Slf4j
public class AbstractController {

    @Autowired
    private StaffService staffService;

    protected ResponseEntity create(Try.CheckedSupplier<String> locationSupplier) {

        ResponseEntity response = Try.of(locationSupplier)
                .mapTry((location) ->
                        (ResponseEntity) ResponseEntity.created(new URI(location)).build()
                ).onFailure((t) ->
                        log.error("An error has occurred while trying to create", t)
                ).getOrElseGet((t) ->
                        ResponseEntity.notFound().build()
                );


        return response;

    }

    protected ResponseEntity update(Try.CheckedSupplier<Boolean> updateSupplier) {

        ResponseEntity response = Try.of(updateSupplier)
                .mapTry((result) ->
                        ResponseEntity.ok(result)
                ).onFailure((t) ->
                        log.error("An error has occurred while trying to update", t)
                ).getOrElseGet((t) ->
                        ResponseEntity.notFound().build()
                );

        return response;

    }

    protected ResponseEntity delete(Try.CheckedSupplier<Boolean> deleteSupplier) {

        ResponseEntity response = Try.of(deleteSupplier)
                .mapTry((result) ->
                        ResponseEntity.ok(result)
                ).onFailure((t) ->
                        log.error("An error has occurred while trying to delete", t)
                ).getOrElseGet((t) ->
                        ResponseEntity.notFound().build()
                );

        return response;

    }

    protected <T> ResponseEntity<T> find(Try.CheckedSupplier<T> findSupplier) {

        ResponseEntity<T> response = Try.of(findSupplier)
                .mapTry((result) ->
                        ResponseEntity.ok(result)
                ).onFailure((t) ->
                        log.error("An error has occurred while trying to find", t)
                ).getOrElseGet((t) ->
                        ResponseEntity.notFound().build()
                );

        return response;

    }

    protected <T> ResponseEntity<List<T>> list(Try.CheckedSupplier<List<T>> findSupplier) {

        ResponseEntity<List<T>> response = Try.of(findSupplier)
                .mapTry((result) ->
                        ResponseEntity.ok(result)
                ).onFailure((t) ->
                        log.error("An error has occurred while trying to list", t)
                ).getOrElseGet((t) ->
                        ResponseEntity.notFound().build()
                );

        return response;

    }

    protected ResponseEntity unprocessableEntity(String message) {
        return ResponseEntity.unprocessableEntity().body(Error.of(message));
    }

    @AllArgsConstructor(staticName = "of")
    static @Data class Error {
        private String message;
    }

    @Builder
    public static @Data class SimpleStringValue {
        private String value;
    }

    @AllArgsConstructor(staticName = "of")
    @Builder
    public static @Data class SimpleIntegerValue {
        private Integer value;

        public void add(Integer v) {
            value += v;
        }

        public void add(Long v) {
            value += v.intValue();
        }

    }

    @AllArgsConstructor(staticName = "of")
    @Builder
    public static @Data class SimpleBooleanValue {
        private Boolean value;
    }

    public User getUser(HttpServletRequest request) {
        return User.of(((waffle.spring.WindowsAuthenticationToken) request.getUserPrincipal()).getName(),
                ((waffle.spring.WindowsAuthenticationToken) request.getUserPrincipal()).getAuthorities());
    }

    public Staff getStaff(HttpServletRequest request) {
        return staffService.findByLogin(getUser(request).getLogin());
    }

    public Boolean isUserInRole(HttpServletRequest request, String role) {
        return getUser(request).getAuthorities().stream().anyMatch(a -> a.getAuthority().replaceAll("ROLE_WARNNT\\\\", "").equalsIgnoreCase(role));
    }

    public Boolean isUserAdmin(HttpServletRequest request) {
        return isUserInRole(request, "HR Portal Admins");
    }

}
