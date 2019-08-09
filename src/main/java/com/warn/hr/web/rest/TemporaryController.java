package com.warn.hr.web.rest;

import com.warn.hr.business.TemporaryService;
import com.warn.hr.persistence.entity.hr.staff.Temporary;
import com.warn.hr.persistence.entity.hr.staff.common.Staff;
import com.warn.hr.web.rest.common.AbstractController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static javaslang.API.*;

@Slf4j
@RestController
public class TemporaryController extends AbstractController {

    @Autowired
    private TemporaryService temporaryService;

    @PostMapping("/hr/api/v1/temporary")
    public @ResponseBody ResponseEntity create(@RequestBody Temporary entity, HttpServletRequest request) {

       if(Objects.isNull(entity.getPreferredName())) {
           entity.setPreferredName(entity.getFirstName());
       }

        return create(() -> String.format("/hr/api/v1/temporary/%d", temporaryService.insert(getStaff(request), entity)));
    }


    @PutMapping("/hr/api/v1/temporary/{id}")
    public @ResponseBody ResponseEntity update(@PathVariable("id") Long id, @RequestBody Temporary entity, HttpServletRequest request) {
        entity.setId(id);
        return update(() -> temporaryService.update(getStaff(request), entity));
    }

    @PutMapping("/hr/api/v1/temporary/term/{id}")
    public @ResponseBody ResponseEntity delete(@PathVariable("id") Long id, @RequestBody Temporary entity, HttpServletRequest request) {
        return Match(temporaryService.termTemporary(getStaff(request), temporaryService.findById(id), entity.getContractEndDate())).of(Case($(Boolean::booleanValue), delete(() -> Boolean.TRUE)),
                Case($(), unprocessableEntity("The temporary cannot be termed! There are Employees or Staff Resources associated with this employee.")));
    }

    @GetMapping("/hr/api/v1/temporary/{id}")
    public @ResponseBody ResponseEntity<Temporary> findById(@PathVariable("id") Long id) {
        return find(() -> temporaryService.findById(id));
    }

    @GetMapping("/hr/api/v1/temporary/list")
    public @ResponseBody ResponseEntity<List<Temporary>> list(Temporary entity,
                                                             @RequestParam(name = "startHireDate", required = false)
                                                             @DateTimeFormat(pattern="yyyy-MM-dd") Date startHireDate,
                                                             @RequestParam(name = "endHireDate", required = false)
                                                             @DateTimeFormat(pattern="yyyy-MM-dd") Date endHireDate) {

        entity.setStatus(Staff.Status.ACTIVE);

        return list(() -> temporaryService.list(entity, startHireDate, endHireDate));
    }

    @GetMapping("/hr/api/v1/temporary/list-all")
    public @ResponseBody ResponseEntity<List<Temporary>> listAll() {

        Temporary entity = new Temporary();
        entity.setStatus(Staff.Status.ACTIVE);

        return list(() -> temporaryService.list(entity, null, null));
    }

}
