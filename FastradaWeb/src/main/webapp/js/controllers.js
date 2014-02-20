function SessionDetailController($scope, Session) {
    $scope.sessions = Session.query();
}

function HomeController($scope, Session) {
    $scope.sessions = Session.query();
}