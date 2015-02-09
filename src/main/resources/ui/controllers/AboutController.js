'use strict';

var AboutController = function($scope, $http) {
    $scope.message = 'Look! I am a NEW!!!! about page using new Controller.';

    $scope.fetchAbout = function() {

        $http.get('/about').success(function(about){
            $scope.about = about;
        });
    }

    $scope.fetchAbout();
}