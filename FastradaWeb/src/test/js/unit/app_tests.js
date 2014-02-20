toastr = {
    success: function (msg) {
        console.log(msg);
    }
}
describe('Controllers', function () {
    beforeEach(module('fastrada.services'));

    beforeEach(function () {
        this.addMatchers({
            toEqualData: function (expected) {
                return angular.equals(this.actual, expected);
            }
        });
    });

    describe('HomeController', function () {
        var scope, controller, http;

        beforeEach(inject(function ($httpBackend, $rootScope, $controller) {
            http = $httpBackend;
            http.expectGET('/api/sessions').respond([
                0, 1, 2, 3, 4, 5, 6, 7, 8, 9
            ]);
        }));

        it('should return list from 0 to 9', function () {

        });
    });
});