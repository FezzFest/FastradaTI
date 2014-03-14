angular.module("fastrada.services", []).
    service('SessionData', ['$http', function ($http) {

        this.getSessions = function () {
            return $http.get('/api/sessions');
        };

        this.getSessionParameter = function (id, parameter) {
            return $http.get('/api/sessions/' + id + '/' + parameter);
        };

        this.getSessionParameters = function (id) {
            return $http.get('/api/sessions/' + id);
        };

        this.deleteSession = function (id) {
            return $http.delete('api/sessions/delete/' + id);
        };

        this.getGpsData = function (id) {
            return $http.get('api/sessions/gps/' + id);
        };
    }]);