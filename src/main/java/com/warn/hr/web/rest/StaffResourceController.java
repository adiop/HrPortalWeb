package com.warn.hr.web.rest;

import com.warn.hr.business.StaffResourceService;
import com.warn.hr.persistence.entity.hr.StaffResource;
import com.warn.hr.persistence.entity.hr.staff.Technician;
import com.warn.hr.web.rest.common.AbstractController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static javaslang.API.*;

@Slf4j
@RestController
public class StaffResourceController extends AbstractController {

    @Autowired
    private StaffResourceService staffResourceService;

    @PostMapping("/hr/api/v1/staff-resource/")
    public @ResponseBody ResponseEntity create(@RequestBody StaffResource staffResource) {
        return create(() -> String.format("/hr/api/v1/staff-resource/%d", staffResourceService.insert(staffResource)));
    }


    @PutMapping("/hr/api/v1/staff-resource/{id}")
    public @ResponseBody ResponseEntity update(@PathVariable("id") Long id, @RequestBody StaffResource entity) {
        entity.setId(id);
        return update(() -> staffResourceService.update(entity));
    }

    @DeleteMapping("/hr/api/v1/staff-resource/{id}")
    public @ResponseBody ResponseEntity delete(@PathVariable("id") Long id) {

        StaffResource staffResource = new StaffResource();
        staffResource.setId(id);

        return Match(staffResourceService.delete(staffResource)).of(Case($(l -> l.booleanValue()), delete(() -> Boolean.TRUE)),
                                                                    Case($(), unprocessableEntity("The staff resource cannot be removed! There are Employees or Job Titles associated with this staff resource.")));

    }

    @GetMapping("/hr/api/v1/staff-resource/{id}")
    public ResponseEntity<StaffResource> findById(@PathVariable("id") Long id) {
        return find(() -> staffResourceService.findById(id));
    }

    @GetMapping("/hr/api/v1/staff-resource/list")
    public ResponseEntity<List<StaffResource>> list(StaffResource staffResource){
        return list(() -> staffResourceService.list(staffResource));
    }

    @GetMapping("/hr/api/v1/staff-resource/list-all")
    public ResponseEntity<List<StaffResource>> listAll() {
        return list(() -> staffResourceService.list(new StaffResource()));
    }

}
