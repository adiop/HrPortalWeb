class StaffResourceForm extends Form {

    static newInstance() {
        return new StaffResourceForm();
    }

    constructor() {
        super();
        this.reset();
    }

    selectDefaultResource(value) {
        this.defaultResource = this.defaultResourceList.filter(t => t.value === value)[0];
    }

    loadDefaultResourceList(optionList) {
        this.defaultResourceList = Common.mapToSelect(optionList, l => l.label, v => v.value);
    }

    selectType(value) {
        this.type = this.typeList.filter(t => t.value === value)[0];
    }

    loadTypeList(optionList) {
        this.typeList = Common.mapToSelect(optionList, l => l.label, v => v.value);
    }

    reset() {

        this.name = '';
        this.description = '';

        //Default resource
        this.defaultResource = {label: '', value: ''};

        //Types
        this.type = {label: '', value: ''};

        //Technicians
        this.technician = null;

    }

}
