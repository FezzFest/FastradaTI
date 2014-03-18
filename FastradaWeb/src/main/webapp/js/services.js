angular.module("fastrada.services", [])
    .factory('SessionIdFactory', [function(){
        var sessionId = null;
        return {
            get:function(){
                return sessionId;
            },
            set:function(newId){
                sessionId = newId;
            }
        }
    }])
    .service('SessionData', ['$http', function ($http) {

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
        this.getSessionMetaData = function (id) {
            return $http.get('api/sessions/metadata/' + id);
        };
    }]);