describe('Controllers', function () {
    describe('users loads page', function () {
        var scope, ctrl;

        beforeEach(inject(function ($controller, $rootScope) {
            scope = $rootScope.$new();
            ctrl = $controller('HomeController', {$scope: scope});
        }));

        it('should be 12', function () {
            expect(scope.sessions.length).toEqual(12);
        });
    });
});