angular.module("fastrada.services", ["ngResource"]).
    factory('Session', function ($resource) {
        var Session = $resource('/api/sessions/:sessionId', {sessionId: '@id'},
            {update: {method: 'PUT'}});
        Session.prototype.isNew = function(){
            return (typeof(this.id) === 'undefined');
        }
        return Session;
    });