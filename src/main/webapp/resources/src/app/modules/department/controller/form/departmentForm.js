'use strict';

class DepartmentForm extends Form {

    static newInstance() {
        return new DepartmentForm();
    }

    constructor() {
        super();
        this.reset();
    }

    reset() {
        this.name = '';
        this.departmentCode = '';
        this.description = '';
    }

}

