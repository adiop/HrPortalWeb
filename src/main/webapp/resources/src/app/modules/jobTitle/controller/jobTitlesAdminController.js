'use strict';

//Job titles controller
app.controller('jobTitlesAdminController', function($scope, $timeout, jobTitlesService, departmentsService, staffResourceService) {

    const ctrl = this;
    const svc = jobTitlesService;

    //View
    {

        ctrl.view = {
            form: JobTitleForm.newInstance()
        };

        ctrl.view.show = (id) => {

            // Clean the insert form
            ctrl.resetForms(ctrl.view.form);

            // Load the form
            svc.findById(id, ctrl.view.form);

        }

    }

    //Insert
    {

        ctrl.insert =  {
            form: JobTitleForm.newInstance()
        };

        ctrl.insert.show =  () => {

            // Clean the insert form
            ctrl.resetForms(ctrl.insert.form, ctrl.updateForm);

        }

        ctrl.insert.do = () => {
            ctrl.doThenClearSearch(() => svc.insert(ctrl.insert.form, ctrl.messages));
        }

    }

    // Update
    {

        ctrl.update = {
            form: JobTitleForm.newInstance()
        };

        ctrl.update.show = (id) => {

            // Clean the update form
            ctrl.resetForms(ctrl.update.form, ctrl.updateForm);

            // Load the form
            svc.findById(id, ctrl.update.form);

        }

        ctrl.update.do = () => {
            ctrl.doThenClearSearch(() => svc.update(ctrl.update.form, ctrl.messages));
        }

    }

    // Delete
    {

        ctrl.delete = {
            form: JobTitleForm.newInstance()
        }

        ctrl.delete.show = (id) => {

            // Clean the update form
            ctrl.resetForms(ctrl.delete.form);

            // Load the form
            ctrl.delete.form.id = id;

        }

        ctrl.delete.do = () => {
            ctrl.doThenClearSearch(() => svc.delete(ctrl.delete.form.id, ctrl.messages));
        }

    }

    // Search
    {

        ctrl.search = {
            form: JobTitleForm.newInstance()
        }

        ctrl.search.show = () => {
            ctrl.search.paginatedResult = undefined;
            ctrl.search.form.reset();
        }

        ctrl.search.do = () => {
            ctrl.cleanMessages();
            ctrl.search.paginatedResult = Pagination.of(svc.list(ctrl.search.form));
            ctrl.search.orderByName();
        }

        ctrl.search.orderByName = () => {
            ctrl.search.paginatedResult.sortByStringProperty('name');
        }

    }

    // Initialize the forms
    ctrl.initForm = (form) => {
        form.loadDepartmentList(departmentsService.listAll());
        form.loadStaffResourcesList(staffResourceService.listAll());
    }

    // Initialize
    {
        Common.initializeController(ctrl);
        ctrl.initForm(ctrl.search.form);
        ctrl.initForm(ctrl.insert.form);
        ctrl.initForm(ctrl.update.form);
        ctrl.initForm(ctrl.view.form);
    }

});