angular.module('fastrada', ['fastrada.services']).
    config(function ($routeProvider) {
        $routeProvider
            .when('/', {templateUrl: '/views/partials/home.html', controller: HomeController})
            .when('/:sessionId', {templateUrl: '/views/partials/detail.html', controller: SessionDetailController});
    });


