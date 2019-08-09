'use strict';

//Employee controller
app.controller('adpReportController', function($scope, $timeout, $location, $filter, adpReportService, staffService, employeeService) {

    const ctrl = this;
    const svc = adpReportService;

    // Upload
    {

        ctrl.upload = {
            form: AdpReportForm.newInstance()
        }

        ctrl.upload.show = () => {

            // Clean the insert form
            ctrl.resetForms(ctrl.upload.form, ctrl.uploadForm);

        }

        ctrl.upload.do = () => {
            svc.uploadFile(ctrl.upload.form, ctrl.messages);
        }

    }

    // Update
    {

        ctrl.update = {};

        ctrl.update.load = (updatedEmployee) => {

            ctrl.update.adpEmployee = updatedEmployee;

            // Validate the adp employee information
            ctrl.update.validate(ctrl.update.adpEmployee);

        }

        ctrl.update.validate = (adpEmployee) => {

            adpEmployee.isValid = true;
            adpEmployee.errors = {};

            if(adpEmployee.department.id === null || adpEmployee.department.id === undefined) {
                adpEmployee.errors.departmentError = true;
                adpEmployee.isValid = false;
            }

            if(adpEmployee.jobTitle.id === null || adpEmployee.jobTitle.id === undefined) {
                adpEmployee.errors.jobTitleError = true;
                adpEmployee.isValid = false;
            }

            if(adpEmployee.manager.id === null || adpEmployee.manager.id === undefined) {
                adpEmployee.errors.managerError = true;
                adpEmployee.isValid = false;
            }

        }

        ctrl.update.do = () => {

            ctrl.cleanMessages();

            if (ctrl.update.adpEmployee.isValid) {

                let databaseEmployee = ctrl.update.adpEmployee.employee;
                let adpEmployee = ctrl.update.adpEmployee;

                databaseEmployee.firstName = adpEmployee.firstName;
                databaseEmployee.preferredName = adpEmployee.preferredName;
                databaseEmployee.lastName = adpEmployee.lastName;
                databaseEmployee.shift = adpEmployee.shift;

                databaseEmployee.department = adpEmployee.department;
                databaseEmployee.jobTitle = adpEmployee.jobTitle;
                databaseEmployee.manager = adpEmployee.manager;
                databaseEmployee.shift = adpEmployee.shift;

                svc.update(databaseEmployee,
                    ctrl.messages,
                    () => {
                        ctrl.messages.successMessage = `Employee ${adpEmployee.preferredName} ${adpEmployee.lastName} updated with success!`;
                        ctrl.upload.form.map.UPDATED = ctrl.upload.form.map.UPDATED.filter(e => e.employeeNumber != adpEmployee.employeeNumber).slice();
                    },
                    () => {
                        ctrl.messages.successMessage = 'An error has occurred!';
                    });

            }

        }

    }

    // Initialize
    {
        Common.initializeController(ctrl);

        // Initialize the upload form
        ctrl.upload.show();

    }

});





