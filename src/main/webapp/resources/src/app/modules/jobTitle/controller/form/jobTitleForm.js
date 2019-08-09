class JobTitleForm extends Form {

    static newInstance() {
        return new JobTitleForm();
    }

    constructor() {
        super();
        this.reset();
    }

    selectDepartment(value) {
        this.department = this.departmentList.filter(t => t.value === value)[0];
    }

    loadDepartmentList(departmentList) {
        this.departmentList = Common.mapToSelect(departmentList, l => l.name, v => v.id);
    }

    loadStaffResourcesList(staffResourceList) {

        // Initialize the form
        let mapper = r => {
            return {name: r.name, value: r.id, selected: r.isEmployeeDefault};
        }

        this.employeeDefaultResources = staffResourceList.filter(sr => sr.isEmployeeDefault).slice();
        this.applicationAccessList = staffResourceList.filter(sr => sr.type === 'APPLICATION_ACCESS').map(mapper).slice();
        this.assetList = staffResourceList.filter(sr => sr.type === 'ASSET').map(mapper).slice();
        this.emailGroupList = staffResourceList.filter(sr => sr.type === 'EMAIL_GROUP').map(mapper).slice();

    }

    selectStaffResources(value) {

        let isSelected = (r, selectedResources) => {
            return selectedResources.filter(sr => r.value === sr.id).length > 0;
        };

        this.applicationAccessList.forEach(r => r.selected = isSelected(r, value));
        this.assetList.forEach(r => r.selected = isSelected(r, value));
        this.emailGroupList.forEach(r => r.selected = isSelected(r, value));

    }

    selectDefaultEmployeeResources() {

        if(this.employeeDefaultResources) {
            this.selectStaffResources(this.employeeDefaultResources);
        }

    }

    reset() {

        this.name = null;

        //Default Department
        this.department = {label: '', value: ''};

        this.selectDefaultEmployeeResources();

    }

}