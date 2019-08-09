'use strict';

class TemporaryForm extends Form {

    static newInstance() {
        return new TemporaryForm();
    }

    constructor() {
        super();
        this.reset();
    }

    selectDepartment(value) {

        this.department = this.departmentList.filter(t => t.value === value)[0];

        if(value === '') {
            this.jobTitleList = null;
        } else {
            this.jobTitle = {label: '', value: ''};
            this.jobTitleList = Common.mapToSelect((this.allJobTitleList.filter(j => j.department.id === value)), l => l.name, v => v.id);
        }

    }

    loadDepartmentList(departmentList) {
        this.departmentList = Common.mapToSelect(departmentList, l => l.name, v => v.id);
    }

    selectJobTitle(value) {
        this.jobTitle = this.jobTitleList.filter(t => t.value === value)[0];
    }

    loadJobTitleList(allJobTitleList) {
        this.allJobTitleList = allJobTitleList;
    }

    loadStaffResourceList(staffResourceList) {

        if(staffResourceList) {

            // Initialize the form
            let mapper = r => {
                return {name: r.name, value: r.id, selected: r.isEmployeeDefault};
            }

            this.loadEmployeeDefaultStaffResources(staffResourceList);
            this.loadTeamLeaderDefaultStaffResources(staffResourceList);

            this.applicationAccessList = staffResourceList.filter(sr => sr.type === 'APPLICATION_ACCESS').map(mapper).slice();
            this.assetList = staffResourceList.filter(sr => sr.type === 'ASSET').map(mapper).slice();
            this.emailGroupList = staffResourceList.filter(sr => sr.type === 'EMAIL_GROUP').map(mapper).slice();

        }

    }

    selectStaffResources(value) {

        let selectFn = (resourceList) => {
            let selectedResourcesIds = value.map(r => r.id);
            resourceList.forEach(r => {
                r.selected = selectedResourcesIds.includes(r.value);
            });
        };

        selectFn(this.applicationAccessList);
        selectFn(this.assetList);
        selectFn(this.emailGroupList);

    }

    loadEmployeeDefaultStaffResources(staffResourceList) {

        if(staffResourceList) {
            this.employeeDefaultResources = staffResourceList.filter(sr => sr.isEmployeeDefault).slice();
        }

    }

    selectEmployeeDefaultStaffResources() {

        if(this.employeeDefaultResources) {
            this.selectStaffResources(this.employeeDefaultResources);
        }

    }

    loadTeamLeaderDefaultStaffResources(staffResourceList) {

        if(staffResourceList) {
            this.teamLeaderDefaultResources = staffResourceList.filter(sr => sr.isTeamLeaderDefault || sr.isEmployeeDefault).slice();
        }

    }

    selectTeamLeaderDefaultStaffResources() {

        let selectFn = (resourceList) => {
            let teamLeaderDefaultResourcesIds = this.teamLeaderDefaultResources.map(r => r.id);
            resourceList.forEach(r => r.selected = (r.selected || teamLeaderDefaultResourcesIds.includes(r.value)));
        };

        selectFn(this.applicationAccessList);
        selectFn(this.assetList);
        selectFn(this.emailGroupList);

    }

    unselectStaffResources() {

        let unselectFn = (resourceList) => {
            resourceList.forEach(r => r.selected = false);
        };

        unselectFn(this.applicationAccessList);
        unselectFn(this.assetList);
        unselectFn(this.emailGroupList);

    }

    reset() {

        // Step
        this.step = 1;

        // Personal information
        this.image = null;
        this.firstName = null;
        this.preferredName = null;
        this.lastName = null;

        // Employee information
        this.shift = null;
        this.keyCardNumber = null;
        this.temporaryNumber = null;
        this.email = null;
        this.login = null;
        this.phone = null;
        this.teamLeader = false;
        this.contractStartDate = null;
        this.contractEndDate = null;

        this.companyName = null;
        this.department = {label: '', value: ''};
        this.jobTitle = {label: '', value: ''};

        // Manager
        this.manager = null;

        // Term
        this.termType = 'now';

        // Staff resources message
        this.changeStaffResourcesMessage = null;

        // Additional information
        this.flagged = false;
        this.flaggedUntil = null;
        this.flagEmailNotification = null;
        this.comments = null;

        this.selectEmployeeDefaultStaffResources();

    }

}