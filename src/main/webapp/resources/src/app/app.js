const checkRouting = function ($q, $rootScope, $location) {

    $rootScope.notify = (message) => {

        let injector = angular.element('*[ng-app]').injector();

        injector.get("employeeService").resetStorage(true);
        injector.get("temporariesService").resetStorage(true);
        injector.get("staffService").resetStorage(true);
        injector.get("employeeSelectorService").resetStorage(true);
        injector.get("jobTitlesService").resetStorage(true);
        injector.get("departmentsService").resetStorage(true);
        injector.get("staffResourceService").resetStorage(true);

    };

    if(!$rootScope.applicationInitialized) {

        // Init the web socket
        let client = new SockJS('/notify');
        let  stomp = Stomp.over(client);

        stomp.connect({}, function() {
            stomp.subscribe("/topic/notify", $rootScope.notify);
        });

        // Init the services
        $location.path('/setup');

    }

}

const app = angular.module('hr-app', ["ngRoute", "ngMessages"]);

app.constant('config', {});

app.config(['$routeProvider', function($routeProvider) {

    $routeProvider
        .when("/setup", {
            templateUrl: "/resources/src/app/modules/setup/views/setupApplication.html",
            controller: "setupApplicationController",
            controllerAs: "ctrl",
        })
        .when("/forbidden", {
            templateUrl: "/resources/src/app/modules/setup/views/error/forbidden.html"
        })
        .when("/", {
            templateUrl: "/resources/src/app/modules/portal/views/index.html",
            controller: "portalController",
            controllerAs: "ctrl",
            resolve: {
                factory: checkRouting
            }
        })
        .when("/employee", {
            templateUrl: "/resources/src/app/modules/employee/views/employees.html",
            controller: "employeeAdminController",
            controllerAs: "ctrl",
            resolve: {
                factory: checkRouting
            }
        })
        .when("/adpReport", {
            templateUrl: "/resources/src/app/modules/employeeADPReport/views/employeeADPReport.html",
            controller: "adpReportController",
            controllerAs: "ctrl",
            resolve: {
                factory: checkRouting
            }
        })
        .when("/temporaries", {
            templateUrl: "/resources/src/app/modules/temporaries/views/temporaries.html",
            controller: "temporariesAdminController",
            controllerAs: "ctrl",
            resolve: {
                factory: checkRouting
            }
        })
        .when("/tracker", {
            templateUrl : "/resources/src/app/modules/tracker/views/tasksTracker.html",
            controller: "taskController",
            controllerAs: "ctrl",
            resolve: {
                //factory: checkRouting
            }
        })
        .when("/department", {
            templateUrl: "/resources/src/app/modules/department/views/departments.html",
            controller: "departmentsAdminController",
            controllerAs: "ctrl",
            resolve: {
                factory: checkRouting
            }
        })
        .when("/job-title", {
            templateUrl: "/resources/src/app/modules/jobTitle/views/jobTitles.html",
            controller: "jobTitlesAdminController",
            controllerAs: "ctrl",
            resolve: {
                factory: checkRouting
            }
        })
        .when("/staffResource", {
            templateUrl : "/resources/src/app/modules/staffResource/views/staffResources.html",
            controller: "staffResourceAdminController",
            controllerAs: "ctrl",
            resolve: {
                factory: checkRouting
            }
        })
        .when("/tasks", {
            templateUrl : "/resources/src/app/modules/task/views/tasks.html",
            controller: "taskController",
            controllerAs: "ctrl",
            resolve: {
                //factory: checkRouting
            }
        })
        .when("/reports", {
            templateUrl : "/resources/src/app/modules/reports/views/reports.html",
            controller: "reportsController",
            controllerAs: "ctrl",
            resolve: {
                //factory: checkRouting
            }
        });

}]);
