angular.module('fastrada', ['fastrada.services', 'googlechart','ui.slider']).
    config(function ($routeProvider) {
        $routeProvider
            .when('/', {templateUrl: '/views/partials/home.html', controller: HomeController})
            .when('/sessiondetail/:sessionId', {templateUrl: '/views/partials/detail.html', controller: SessionDetailController})
            .when('/sessiondetail/:sessionId/:parameterName', {templateUrl: '/views/partials/detail.html', controller: SessionDetailController});
    });

