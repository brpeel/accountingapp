'use strict';

var AboutController = function($scope, $http) {

    $scope.fetchAbout = function() {

        $http.get('/open/about').success(function(about){
            $scope.about = about;
        });
    }

    $scope.fetchAbout();
}