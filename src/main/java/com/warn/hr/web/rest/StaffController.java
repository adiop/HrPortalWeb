package com.warn.hr.web.rest;

import com.warn.hr.business.ActiveDirectoryService;
import com.warn.hr.business.StaffService;
import com.warn.hr.persistence.entity.hr.staff.common.Staff;
import com.warn.hr.web.rest.common.AbstractController;
import javaslang.control.Try;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
public class StaffController extends AbstractController {

    private static final Integer MAX_IMAGE_SIZE = 1024 * 100;

    @Autowired
    private StaffService staffService;

    @Autowired
    private ActiveDirectoryService ldapDirectoryService;

    @GetMapping("/hr/api/v1/staff/{id}")
    public @ResponseBody ResponseEntity<Staff> findById(@PathVariable("id") Long id) {
        return find(() -> staffService.findById(id));
    }

    @GetMapping("/hr/api/v1/staff/image/{id}")
    public @ResponseBody ResponseEntity<FileResponse> findImageByStaffId(@PathVariable("id") Long id) {
        return find(() -> FileResponse.of(Boolean.TRUE, null, staffService.findImageByStaffId(id)));
    }

    @PostMapping("/hr/api/v1/staff/upload-image")
    public ResponseEntity convertFileToBase64(@RequestParam("file") MultipartFile file) throws IOException {

        ResponseEntity<FileResponse> response = Try.of(() -> {

            byte[] bytes = file.getBytes();

            if(bytes.length > MAX_IMAGE_SIZE) {
                return FileResponse.of(Boolean.FALSE,
                        "The image size can't exceed 100 KB",
                        null);
            } else {
                byte[] encoded = Base64.encodeBase64(bytes);
                String imageString = new String(encoded);

                return FileResponse.of(Boolean.TRUE,
                        null,
                        "data:" + file.getContentType() + ";base64," + imageString);
            }

        }).mapTry((result) ->
                ResponseEntity.ok(result)
        ).onFailure((t) ->
                log.error("An error has occurred while trying to convert the image", t)
        ).getOrElseGet((t) ->
                ResponseEntity.notFound().build()
        );

        return response;
    }

    @GetMapping("/hr/api/v1/staff/list-all")
    public @ResponseBody ResponseEntity<List<Staff>> listAll() {
        return list(() -> staffService.listAll());
    }

    @GetMapping("/hr/api/v1/staff/validation/login/{login}")
    public @ResponseBody ResponseEntity<SimpleBooleanValue> validateLogin(@PathVariable("login") String login) {
        return find(() -> SimpleBooleanValue.of(Optional
                                                .ofNullable(staffService.findByLogin(login))
                                                .map(s -> Boolean.TRUE)
                                                .orElseGet(() -> Boolean.FALSE)));
    }

    @GetMapping("/hr/api/v1/staff/validation/staff-number/{staffNumber}")
    public @ResponseBody ResponseEntity<SimpleBooleanValue> validateStaffNumber(@PathVariable("staffNumber") String staffNumber) {
        return find(() -> SimpleBooleanValue.of(Optional
                .ofNullable(staffService.findByStaffNumber(staffNumber))
                .map(s -> Boolean.TRUE)
                .orElseGet(() -> Boolean.FALSE)));
    }

    @AllArgsConstructor(staticName = "of")
    static @Data class FileResponse {
        private Boolean isSuccess;
        private String errorMessage;
        private String base64File;
    }

}
