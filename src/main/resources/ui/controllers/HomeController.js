'use strict';
var MenuController = function($rootScope, $scope, $http, $window, $location) {
    $rootScope.menuItems = []
    $scope.fetchMenu = function () {

        console.log('Fetch Menu Items');
        $http.get('/api/mainmenu/actions')
            .success(function (data, status, headers, config) {
                console.log(data)
                $rootScope.menuItems = data.menuItems;
            });
    }


    $scope.fetchMenu();
  };