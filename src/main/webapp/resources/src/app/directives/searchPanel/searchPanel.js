app.directive("searchPanel", function() {
    return {
        restrict : "E",
        scope: {
            paginatedResult: '=paginatedResult'
        },
        transclude: {
            'content': 'searchPanelContent'
        },
        templateUrl : "/resources/src/app/directives/searchPanel/template/searchPanel.html"
    };
});
