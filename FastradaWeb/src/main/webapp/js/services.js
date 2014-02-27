angular.module("fastrada.services", []).
    service('SessionData', ['$http', function ($http) {

        this.getSessions = function (value) {
            return $http.get('/api/sessions', value);
        };
    }]);