'use strict';

// Setup application controller
app.controller('setupApplicationController', function ($rootScope, $scope, $http, $q, $timeout, $location, employeeService, temporariesService, staffService, employeeSelectorService, jobTitlesService, departmentsService, staffResourceService) {

    const ctrl = this;

    ctrl.verifyUserAccess = () => {

        Request.get(`/hr/api/v1/user/staff`).call(response => {

            Response.of(response.status)
                .ok(() => {
                    ctrl.loadData();
                })
                .forbidden(() => {
                    $location.path('/forbidden');
                });

        });

    }

    ctrl.loadData = () => {

        $timeout(() => {

            // Initialize the services
            employeeService.resetStorage(true);
            temporariesService.resetStorage(true);
            staffService.resetStorage(true);
            employeeSelectorService.resetStorage(true);
            departmentsService.resetStorage(true);
            jobTitlesService.resetStorage(true);
            staffResourceService.resetStorage(true);

            // Set the scope as initialized
            $rootScope.applicationInitialized = true;

            // Redirect the user to the main page
            $location.path('/');

        }, 5000);

    };


    ctrl.initialize = () => {
        // Init the configuration of the requests
        Request.config($http,$q);
    };

    {
        ctrl.initialize();
        ctrl.verifyUserAccess();
    }

});