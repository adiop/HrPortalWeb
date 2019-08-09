app.directive("pageTemplate", function() {
    return {
        restrict : "E",
        transclude: {
            'title': 'pageTitle',
            'rightMenu': '?pageRightMenu',
            'content': 'pageContent'
        },
        templateUrl : "/resources/src/app/directives/pageTemplate/template/pageTemplate.html"
    };
});
