'use strict';

//Temporary controller
app.controller('temporariesAdminController', function($scope, $timeout, $location, $filter, temporariesService, staffService, departmentsService, jobTitlesService, staffResourceService, userService) {

    const ctrl = this;
    const svc = temporariesService;
    const departments = Common.mapToSelect(departmentsService.listAll(), l => l.name, v => v.id);

    // Common
    {

        ctrl.uploadPicture = (form) => {
            staffService.uploadPicture(form, form.pictureFile);
        }

        ctrl.loadPicture = (Temporary) => {
            staffService.loadStaffImage(Temporary);
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
                ctrl.delete.form.contractEndDate = date;

            } else if(form.termType === 'endOfDay') {

                let date = new Date();
                date.setDate(date.getDate() + 1);
                date.setHours(0, 0, 0, 0);

                ctrl.delete.form.contractEndDate = date;

            }

        }

    }

    //View
    {

        ctrl.view = {
            form: TemporaryForm.newInstance()
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
            form: TemporaryForm.newInstance()
        }

        ctrl.insert.show = () => {

            ctrl.messages.invalidLoginErrorMessage = null;
            ctrl.messages.invalidStaffNumberErrorMessage = null;

            // Clean the insert form
            ctrl.resetForms(ctrl.insert.form, ctrl.insertForm);

        }

        ctrl.insert.validateTemporaryLogin = () => {

            ctrl.messages.invalidLoginErrorMessage = null;

            let login = ctrl.insert.form.login;

            if(login) {
                svc.validateLogin(login, ctrl.messages);
            }

        }

        ctrl.insert.validateTemporaryNumber = () => {

            ctrl.messages.invalidStaffNumberErrorMessage = null;

            let temporaryNumber = ctrl.insert.form.temporaryNumber;

            if(temporaryNumber) {
                svc.validateStaffNumber(temporaryNumber, ctrl.messages);
            }

        }

        ctrl.insert.do = () => {
            ctrl.doThenClearSearch(() => svc.insert(ctrl.insert.form, ctrl.messages));
        }

    }

    // Update
    {


        ctrl.update = {
            form: TemporaryForm.newInstance()
        }

        ctrl.update.show = (id) => {

            ctrl.editTemporaryNumber = false;
            ctrl.editTemporaryLogin = false;
            ctrl.messages.invalidLoginErrorMessage = null;
            ctrl.messages.invalidStaffNumberErrorMessage = null;


            // Clean the update form
            ctrl.resetForms(ctrl.update.form, ctrl.updateForm);

            svc.findById(id, ctrl.update.form);

        }

        ctrl.update.onEditTemporaryLogin = () => {

            ctrl.editTemporaryLogin = !ctrl.editTemporaryLogin;

            if(ctrl.editTemporaryLogin) {
                ctrl.update.form.oldTemporaryLogin = ctrl.update.form.login;
            }

        }

        ctrl.update.validateTemporaryLogin = () => {

            ctrl.messages.invalidLoginErrorMessage = null;

            let login = ctrl.update.form.login;

            if(login && login !== ctrl.update.form.oldTemporaryLogin) {
                svc.validateLogin(login, ctrl.messages);
            }

        }

        ctrl.update.onEditTemporaryNumber = () => {

            ctrl.editTemporaryNumber = !ctrl.editTemporaryNumber;

            if(ctrl.editTemporaryNumber) {
                ctrl.update.form.oldTemporaryNumber = ctrl.update.form.temporaryNumber;
            }

        }

        ctrl.update.validateTemporaryNumber = () => {

            ctrl.messages.invalidStaffNumberErrorMessage = null;

            let temporaryNumber = ctrl.update.form.temporaryNumber;

            if(temporaryNumber && temporaryNumber !== ctrl.update.form.oldTemporaryNumber) {
                svc.validateStaffNumber(temporaryNumber, ctrl.messages);
            }

        }

        ctrl.update.do = () => {
            ctrl.doThenClearSearch(() => svc.update(ctrl.update.form, ctrl.messages));
        }


    }

    // Hire
    {

        ctrl.hire = {
            form: TemporaryForm.newInstance()
        }

        ctrl.hire.show = (id) => {

            // Clean the hire form
            ctrl.resetForms(ctrl.hire.form);

            // Update the hire date
            ctrl.hire.form.hireDate = new Date();

            // Load the form
            svc.findById(id, ctrl.hire.form);

        }

        ctrl.hire.do = () => {
            console.log('hire do', ctrl.hire.form)
            ctrl.doThenClearSearch(() => svc.hire(ctrl.hire.form, ctrl.messages));
        }

    }

    // Delete
    {

        ctrl.delete = {
            form: TemporaryForm.newInstance()
        }

        ctrl.delete.show = (id) => {

            // Clean the update form
            ctrl.resetForms(ctrl.delete.form);

            // Load the form
            ctrl.delete.form.id = id;
            ctrl.delete.form.contractEndDate = new Date();

        }

        ctrl.delete.do = () => {
            ctrl.doThenClearSearch(() => svc.delete(ctrl.delete.form, ctrl.messages));
        }

    }

    //Search
    {
        ctrl.search = {
            form: TemporaryForm.newInstance()
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

        ctrl.search.orderByContractStartDate = () => {
            ctrl.search.paginatedResult.sortByStringProperty('contractStartDate');
        }

        ctrl.search.orderByContractEndDate = () => {
            ctrl.search.paginatedResult.sortByStringProperty('contractEndDate');
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
        ctrl.initializeForm(ctrl.hire.form);

        // Load the logged user in the control
        userService.loadUser(ctrl);

    }


});





