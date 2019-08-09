'use strict';

// Reports controller
app.controller('reportsController', function ($rootScope, $scope, reportsService) {

    const ctrl = this;
    const svc = reportsService;

    //Load reports
    {

        ctrl.search = {
            form: ReportsForm.newInstance()
        };

        ctrl.search.do = () => {

            // Load the reports
            svc.loadTasksTotal(ctrl.search.form);
            svc.loadTasksByMonthYear(ctrl.search.form);
            svc.loadTasksByDate(ctrl.search.form);
            svc.loadTasksByDepartment(ctrl.search.form);
            svc.loadTasksByJobTitle(ctrl.search.form);

            // Set the result initial and final dates
            ctrl.search.form.result.initialDate = ctrl.search.form.initialDate;
            ctrl.search.form.result.finalDate = ctrl.search.form.finalDate;

        }

    }

    // Initialize
    {
        Common.initializeController(ctrl);
        ctrl.search.do();
    }

});