'use strict';

// Staff resource controller
app.controller('staffResourceAdminController', function ($scope, staffResourceService, employeeService) {

    const ctrl = this;
    const svc = staffResourceService;

    // Common functions
    {

        ctrl.selectTechnician = (form, selectedTechnician) => {
            form.technician = selectedTechnician;
        }

    }

    //View
    {

        ctrl.view = {
            form: StaffResourceForm.newInstance()
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

        ctrl.insert = {
            form: StaffResourceForm.newInstance()
        }

        ctrl.insert.show =  () => {

            // Clean the insert form
            ctrl.resetForms(ctrl.insert.form, ctrl.updateForm);

        }

        ctrl.insert.do = () => {
            ctrl.doThenClearSearch(() => svc.insert(ctrl.insert.form, ctrl.messages));
        }

    }

    //Update
    {

        ctrl.update = {
            form: StaffResourceForm.newInstance()
        }

        ctrl.update.show =  (id) => {

            // Clean the update form
            ctrl.resetForms(ctrl.update.form, ctrl.updateForm);

            // Load the form
            svc.findById(id, ctrl.update.form);


        }

        ctrl.update.do = () => {
            ctrl.doThenClearSearch(() => svc.update(ctrl.update.form, ctrl.messages));
        }

    }

    //Delete
    {

        ctrl.delete = {
            form: StaffResourceForm.newInstance()
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

    //Search
    {

        ctrl.search = {
            form: StaffResourceForm.newInstance()
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

        ctrl.search.orderByType = () => {
            ctrl.search.paginatedResult.sortByStringProperty('type');
        }

    }

    // Initialize the forms
    ctrl.initForm = (form) => {
        form.loadTypeList(svc.listAllTypes());
        form.loadDefaultResourceList(svc.listAllDefaults());
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


