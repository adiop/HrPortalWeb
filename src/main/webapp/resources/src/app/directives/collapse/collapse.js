app.directive("collapse", function() {
    return {
        restrict : "E",
        scope: {
            title: '=collapseTitle',
            id: '=collapseId'
        },
        transclude: {
            'content': 'collapseContent'
        },
        templateUrl : "/resources/src/app/directives/collapse/template/collapse.html"
    };
});
