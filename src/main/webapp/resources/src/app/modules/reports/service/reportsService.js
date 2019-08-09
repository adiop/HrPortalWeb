'use strict';

//Employee service
app.service('reportsService', function(config, $http, $q) {

    const svc = this;

    svc.loadTasksTotal= (form) => {

        form.loadingTasksTotal = true;

        Request.get(`/hr/api/v1/reports/tasks-total?initialDate=${svc.formatDate(form.initialDate)}&finalDate=${svc.formatDate(form.finalDate)}`).call((response) => {

            form.loadingTasksTotal = false;

            Response.of(response.status).ok(() => {
                form.tasksTotal = response.data;
            });

        });

    }

    svc.loadTasksByMonthYear = (form) => {

        form.loadingTasksByMonthYear = true;

        Request.get(`/hr/api/v1/reports/tasks-by-month-year?initialDate=${svc.formatDate(form.initialDate)}&finalDate=${svc.formatDate(form.finalDate)}`).call((response) => {

            form.loadingTasksByMonthYear = false;

            Response.of(response.status).ok(() => {

                form.tasksByMonthYear = response.data;

                let tasksByMonthYearConfig = {
                    type: 'line',
                    data: {
                        labels: form.tasksByMonthYear.taskByMonthYear.map((dataItem) => dataItem.name),
                        datasets: [{
                            label: 'Hired Staff',
                            backgroundColor: window.chartColors.green,
                            borderColor: window.chartColors.green,
                            data: form.tasksByMonthYear.taskByMonthYear.map((dataItem) => dataItem.newStaffTasks),
                            fill: false,
                        },
                            {
                                label: 'Updated Staff',
                                backgroundColor: window.chartColors.yellow,
                                borderColor: window.chartColors.yellow,
                                data: form.tasksByMonthYear.taskByMonthYear.map((dataItem) => dataItem.updateStaffTasks),
                                fill: false,
                            },
                            {
                                label: 'Termed Staff',
                                backgroundColor: window.chartColors.red,
                                borderColor: window.chartColors.red,
                                data: form.tasksByMonthYear.taskByMonthYear.map((dataItem) => dataItem.termStaffTasks),
                                fill: false,
                            }]
                    },
                    options: {
                        responsive: true,
                        tooltips: {
                            mode: 'index',
                            intersect: false,
                        },
                        hover: {
                            mode: 'nearest',
                            intersect: true
                        },
                        scales: {
                            xAxes: [{
                                display: true,
                                scaleLabel: {
                                    display: true,
                                    labelString: 'Months'
                                }
                            }],
                            yAxes: [{
                                display: true,
                                scaleLabel: {
                                    display: true,
                                    labelString: 'Quantity'
                                }
                            }]
                        }
                    }
                };

                $('#tasksByMonthYearDiv').html('');
                $('#tasksByMonthYearDiv').html('<canvas id="tasksByMonthYear"></canvas>');

                let canvas = document.getElementById('tasksByMonthYear');
                let canvasContext = canvas.getContext('2d');
                let timeLine = new Chart(canvasContext, tasksByMonthYearConfig);

            });

        });

    }

    svc.loadTasksByDate = (form) => {

        form.loadingTasksByDate = true;

        Request.get(`/hr/api/v1/reports/tasks-by-date?initialDate=${svc.formatDate(form.initialDate)}&finalDate=${svc.formatDate(form.finalDate)}`).call((response) => {

            form.loadingTasksByDate = false;

            Response.of(response.status).ok(() => {

                form.tasksByDate = response.data;

                let tasksByDateConfig = {
                    type: 'line',
                    data: {
                        labels: form.tasksByDate.taskByDate.map((dataItem) => dataItem.name),
                        datasets: [{
                                label: 'Hired Staff',
                                backgroundColor: window.chartColors.green,
                                borderColor: window.chartColors.green,
                                data: form.tasksByDate.taskByDate.map((dataItem) => dataItem.newStaffTasks),
                                fill: false,
                            },
                            {
                                label: 'Updated Staff',
                                backgroundColor: window.chartColors.yellow,
                                borderColor: window.chartColors.yellow,
                                data: form.tasksByDate.taskByDate.map((dataItem) => dataItem.updateStaffTasks),
                                fill: false,
                            },
                            {
                                label: 'Termed Staff',
                                backgroundColor: window.chartColors.red,
                                borderColor: window.chartColors.red,
                                data: form.tasksByDate.taskByDate.map((dataItem) => dataItem.termStaffTasks),
                                fill: false,
                            }]
                    },
                    options: {
                        responsive: true,
                        tooltips: {
                            mode: 'index',
                            intersect: false,
                        },
                        hover: {
                            mode: 'nearest',
                            intersect: true
                        },
                        scales: {
                            xAxes: [{
                                display: true,
                                scaleLabel: {
                                    display: true,
                                    labelString: 'Dates'
                                }
                            }],
                            yAxes: [{
                                display: true,
                                scaleLabel: {
                                    display: true,
                                    labelString: 'Quantity'
                                }
                            }]
                        }
                    }
                };

                $('#tasksByDateDiv').html('');
                $('#tasksByDateDiv').html('<canvas id="tasksByDate"></canvas>');

                let canvas = document.getElementById('tasksByDate');
                let canvasContext = canvas.getContext('2d');
                let timeLine = new Chart(canvasContext, tasksByDateConfig);

            });

        });

    }

    svc.loadTasksByDepartment = (form) => {

        form.loadingTasksByDepartment = true;

        Request.get(`/hr/api/v1/reports/tasks-by-department?initialDate=${svc.formatDate(form.initialDate)}&finalDate=${svc.formatDate(form.finalDate)}`).call((response) => {

            form.loadingTasksByDepartment = false;

            Response.of(response.status).ok(() => {

                form.tasksByDepartment = response.data;

                let tasksByDepartmentConfig = {
                    type: 'bar',
                    data: {
                        labels: form.tasksByDepartment.taskByDepartment.filter((dataItem) => dataItem.newStaffTasks > 0 || dataItem.termStaffTasks > 0).map((dataItem) => dataItem.name),
                        datasets: [{
                                label: 'Staff Hired',
                                backgroundColor: window.chartColors.green,
                                borderColor: window.chartColors.green,
                                borderWidth: 1,
                                data: form.tasksByDepartment.taskByDepartment.filter((dataItem) => dataItem.newStaffTasks > 0 || dataItem.termStaffTasks > 0).map((dataItem) => dataItem.newStaffTasks)
                            },
                            // {
                            //     label: 'Staff Updated',
                            //     backgroundColor: window.chartColors.yellow,
                            //     borderColor: window.chartColors.yellow,
                            //     borderWidth: 1,
                            //     data: form.tasksByDepartment.taskByDepartment.map((dataItem) => dataItem.updateStaffTasks)
                            // },
                            {
                                label: 'Staff Termed',
                                backgroundColor: window.chartColors.red,
                                borderColor: window.chartColors.red,
                                borderWidth: 1,
                                data: form.tasksByDepartment.taskByDepartment.filter((dataItem) => dataItem.newStaffTasks > 0 || dataItem.termStaffTasks > 0).map((dataItem) => dataItem.termStaffTasks)
                            }]
                    },
                    options: {
                        responsive: true,
                        legend: {
                            position: 'top',
                        }
                    }
                };

                $('#tasksByDepartmentDiv').html('');
                $('#tasksByDepartmentDiv').html('<canvas id="tasksByDepartment"></canvas>');

                let canvas = document.getElementById('tasksByDepartment');
                let canvasContext = canvas.getContext('2d');
                let barChart = new Chart(canvasContext, tasksByDepartmentConfig);

            });

        });

    }

    svc.loadTasksByJobTitle = (form) => {

        form.loadingTasksByJobTitle = true;

        Request.get(`/hr/api/v1/reports/tasks-by-job-title?initialDate=${svc.formatDate(form.initialDate)}&finalDate=${svc.formatDate(form.finalDate)}`).call((response) => {

            form.loadingTasksByJobTitle = false;

            Response.of(response.status).ok(() => {

                form.tasksByJobTitle = response.data;

                let tasksByJobTitleConfig = {
                    type: 'bar',
                    data: {
                        labels: form.tasksByJobTitle.taskByJobTitle.filter(svc.hiredAndTermedStaffFilter).map((dataItem) => dataItem.name),
                        datasets: [{
                            label: 'Staff Hired',
                            backgroundColor: window.chartColors.green,
                            borderColor: window.chartColors.green,
                            borderWidth: 1,
                            data: form.tasksByJobTitle.taskByJobTitle.filter(svc.hiredAndTermedStaffFilter).map((dataItem) => dataItem.newStaffTasks)
                        },
                            // {
                            //     label: 'Staff Updated',
                            //     backgroundColor: window.chartColors.yellow,
                            //     borderColor: window.chartColors.yellow,
                            //     borderWidth: 1,
                            //     data: form.tasksByDepartment.taskByDepartment.map((dataItem) => dataItem.updateStaffTasks)
                            // },
                            {
                                label: 'Staff Termed',
                                backgroundColor: window.chartColors.red,
                                borderColor: window.chartColors.red,
                                borderWidth: 1,
                                data: form.tasksByJobTitle.taskByJobTitle.filter(svc.hiredAndTermedStaffFilter).map((dataItem) => dataItem.termStaffTasks)
                            }]
                    },
                    options: {
                        responsive: true,
                        legend: {
                            position: 'top',
                        }
                    }
                };

                $('#tasksByJobTitleDiv').html('');
                $('#tasksByJobTitleDiv').html('<canvas id="tasksByJobTitle"></canvas>');

                let canvas = document.getElementById('tasksByJobTitle');
                let canvasContext = canvas.getContext('2d');
                let barChart = new Chart(canvasContext, tasksByJobTitleConfig);

            });

        });

    }

    svc.hiredAndTermedStaffFilter = (dataItem) => dataItem.newStaffTasks > 0 || dataItem.termStaffTasks > 0;

    svc.formatDate = (dtValue) => {
        return (dtValue.getMonth() + 1) + '-' + dtValue.getDate() + '-' + dtValue.getFullYear();
    }

    svc.initialize = () => {

        // Init the configuration of the requests
        Request.config($http,$q);

    };

    // Init the service
    {
        svc.initialize();
    };

});


