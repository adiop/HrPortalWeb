class Pagination {

    static of(values) {

        let p = new Pagination(values);
        p.updateHorizon();
        p.updateSelectedValues();

        return p;

    }

    constructor(values, maxResultsPerPage = 10) {
        this.values = values;
        this.selectedValues = [];
        this.maxResultsPerPage = maxResultsPerPage;
        this.actualPage = 1;
        this.horizon = [];
        this.sortedBy = '';
        this.sortDirection = () => '';
    }

    get pageValues() {
        return this.selectedValues;
    }

    get selectedPage() {
        return this.actualPage;
    }

    get totalPages() {
        return Math.ceil(this.values.length / this.maxResultsPerPage);
    }

    get size() {
        return this.values.length;
    }

    isEmpty() {
        return this.values.length == 0;
    }

    hasNext() {
        return this.actualPage < this.totalPages;
    }

    next() {

        if(this.hasNext()) {
            this.actualPage = this.actualPage + 1;
            this.updateSelectedValues();
            this.updateHorizon();
        }

    }

    hasPrevious() {
        return this.actualPage > 1;
    }

    previous() {

        if(this.hasPrevious()) {
            this.actualPage = this.actualPage - 1;
            this.updateSelectedValues();
            this.updateHorizon();
        }

    }

    updateSelectedValues() {

        let startIndex = (this.actualPage - 1) * this.maxResultsPerPage;
        let lastIndex = this.actualPage * this.maxResultsPerPage;

        // Clear the array
        while(this.selectedValues.length > 0) {
            this.selectedValues.pop();
        }

        // Push the elements into the array
        for(let i = startIndex; i < lastIndex && i < this.values.length; i++) {
            this.selectedValues.push(this.values[i]);
        }

    }

    updateHorizon() {

        let horizonSize = 5;
        let page = this.actualPage;
        let lastPage = this.totalPages;

        // Clear the array
        while(this.horizon.length > 0) {
            this.horizon.pop();
        }

        if(lastPage > horizonSize) {

            let isUpdateHorizon = page % 5 === 1;

            if ((page + horizonSize) > lastPage) {

                for (let i = page; i < lastPage; i++) {
                    this.horizon.push(i);
                }

            } else {

                for (let i = page; i < (page + horizonSize); i++) {
                    this.horizon.push(i);
                }

            }

        } else {

            for(let i = 0; i < lastPage; i++) {
                this.horizon.push(i + 1);
            }

        }

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
        this.values.sort(comparator);
        this.updateSelectedValues();
    }

}