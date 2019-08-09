'use strict';

//Employee controller
app.controller('employeeAdminController', function($scope, $timeout, $location, $filter, employeeService, staffService, departmentsService, jobTitlesService, staffResourceService) {

    const ctrl = this;
    const svc = employeeService;
    const departments = Common.mapToSelect(departmentsService.listAll(), l => l.name, v => v.id);

    // Common
    {

        ctrl.uploadPicture = (form) => {
            staffService.uploadPicture(form, form.pictureFile);
        }

        ctrl.loadPicture = (employee) => {
            staffService.loadStaffImage(employee);
        }

        ctrl.selectManager = (form, manager) => {
            form.manager = manager;
        }

        ctrl.loadStaffResourcesOnJobChange = (form) => {

            let jobTitle = form.jobTitle;

            svc.loadJobTitleStaffResources(jobTitle.value, form);
            form.changeStaffResourcesMessage = true;

        }

        ctrl.onTermTypeChange = (form) => {

            ctrl.delete.form.termDate = null;

            if(form.termType === 'now') {

                let date = new Date();
                ctrl.delete.form.termDate = date;

            } else if(form.termType === 'endOfDay') {

                let date = new Date();
                date.setDate(date.getDate() + 1);
                date.setHours(0, 0, 0, 0);

                ctrl.delete.form.termDate = date;

            }

        }

    }

    //View
    {

        ctrl.view = {
            form: EmployeeForm.newInstance()
        };

        ctrl.view.show = (id) => {

            // Clean the insert form
            ctrl.resetForms(ctrl.view.form);

            // Load the form
            svc.findById(id, ctrl.view.form);

        }

    }

    // Insert
    {

        ctrl.insert = {
            form: EmployeeForm.newInstance()
        }

        ctrl.insert.show = () => {

            // Clean the insert form
            ctrl.resetForms(ctrl.insert.form, ctrl.insertForm);

        }

        ctrl.insert.do = () => {
            ctrl.doThenClearSearch(() => svc.insert(ctrl.insert.form, ctrl.messages));
        }

    }

    // Update
    {


        ctrl.update = {
            form: EmployeeForm.newInstance()
        }

        ctrl.update.show = (id) => {

            // Clean the update form
            ctrl.resetForms(ctrl.update.form, ctrl.updateForm);

            svc.findById(id, ctrl.update.form);

        }

        ctrl.update.do = () => {
            ctrl.doThenClearSearch(() => svc.update(ctrl.update.form, ctrl.messages));
        }


    }

     // Delete
    {

        ctrl.delete = {
            form: EmployeeForm.newInstance()
        }

        ctrl.delete.show = (id) => {

            // Clean the update form
            ctrl.resetForms(ctrl.delete.form);

            // Load the form
            ctrl.delete.form.id = id;
            ctrl.delete.form.termDate = new Date();

        }

        ctrl.delete.do = () => {
            ctrl.doThenClearSearch(() => svc.delete(ctrl.delete.form, ctrl.messages));
        }

    }

    //Search
    {

        ctrl.search = {
            form: EmployeeForm.newInstance()
        }

        ctrl.search.show = () => {
            ctrl.search.paginatedResult = undefined;
            ctrl.search.form.reset();
        }

        ctrl.search.do = () => {
            ctrl.cleanMessages();
            ctrl.search.paginatedResult = Pagination.of(svc.list(ctrl.search.form));
            ctrl.search.orderByPreferredName()

        }

        ctrl.search.orderByPreferredName = () => {
            ctrl.search.paginatedResult.sortByStringProperty('preferredName');
        }

        ctrl.search.orderByHiringDate = () => {
            ctrl.search.paginatedResult.sortByStringProperty('hiringDate');
        }

        ctrl.search.orderByDepartment = () => {
            ctrl.search.paginatedResult.sortByStringProperty('department.name');
        }

        ctrl.search.orderByJobTitle = () => {
            ctrl.search.paginatedResult.sortByStringProperty('jobTitle.name');
        }

    }

    ctrl.initializeForm = (form) => {
        form.loadDepartmentList(departmentsService.listAll());
        form.loadJobTitleList(jobTitlesService.listAll());
        form.loadStaffResourceList(staffResourceService.listAll());
    }

    // Initialize
    {
        Common.initializeController(ctrl);

        ctrl.initializeForm(ctrl.view.form);
        ctrl.initializeForm(ctrl.search.form);
        ctrl.initializeForm(ctrl.insert.form);
        ctrl.initializeForm(ctrl.update.form);

    }


});





