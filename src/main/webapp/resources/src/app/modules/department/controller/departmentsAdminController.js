'use strict';

// departments controller
app.controller('departmentsAdminController', function ($scope, departmentsService) {

    const ctrl = this;
    const svc = departmentsService;

    //View
    {

        ctrl.view = {
            form: DepartmentForm.newInstance()
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
            form: DepartmentForm.newInstance()
        };

        ctrl.insert.show = () => {

            // Clean the insert form
            ctrl.resetForms(ctrl.insert.form, ctrl.insertForm);

        }

        ctrl.insert.do = () => {
            ctrl.doThenClearSearch(() => svc.insert(ctrl.insert.form, ctrl.messages));
        }

    }

    //Update
    {

        ctrl.update = {
            form: DepartmentForm.newInstance()
        }

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

    //Delete
    {

        ctrl.delete = {
            form: DepartmentForm.newInstance()
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
            form: DepartmentForm.newInstance()
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

    // Initialize
    {
        Common.initializeController(ctrl);
    }

});
