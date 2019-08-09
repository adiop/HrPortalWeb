'use strict';

class TaskForm extends Form {

    static newInstance() {
        return new TaskForm();
    }

    constructor() {
        super();
        this.reset();
    }

    isSortedBy(property) {
        return (this.sortedBy === property);
    }

    isSortedAsc() {
        return (this.sortDirection === 'asc');
    }

    sortByStringProperty(property) {

        this.sortDirection = (this.sortedBy === property) ? (this.sortDirection === 'asc' ? 'desc' : 'asc') : ('asc');

        this.sortedBy = property;

        this.sort(property, (a, b) => {

            let nameA = eval('a.'.concat(property)).toUpperCase();
            let nameB = eval('b.'.concat(property)).toUpperCase();

            if(this.sortDirection === 'asc') {

                if (nameA < nameB) {
                    return -1;
                }
                if (nameA > nameB) {
                    return 1;
                }

            } else {

                if (nameA > nameB) {
                    return -1;
                }
                if (nameA < nameB) {
                    return 1;
                }

            }

            return 0;

        })

    }

    sort(property, comparator) {
        this.result.sort(comparator);
    }

    reset() {
        this.isListAll = false;
        this.result = null;
        this.name = null;
    }

}