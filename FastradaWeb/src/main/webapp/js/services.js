angular.module("fastrada.services", []).
    service('SessionData', ['$http', function ($http) {

        this.getSessions = function () {
            return $http.get('/api/sessions');
        };

        this.getSessionParameter = function (id, parameter) {
            return $http.get('/api/sessions/' + id + '/' + parameter);
        };
    }]);