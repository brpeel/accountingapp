'use strict';
var MenuController = function($rootScope, $scope, $http, $window, $location) {
    $rootScope.menuItems = []
    $scope.fetchMenu = function () {

        console.log('Fetch Menu Items');
        $http.get('/api/menu/actions')
            .success(function (data, status, headers, config) {
                console.log(data)
                var items = data.menuItems.sort(comparePermissions);
                console.log(items)
                $rootScope.menuItems = items;
                $rootScope.username = data.username;
            });
    }

    function comparePermissions(a,b) {
        if (a.group_order < b.group_order)
            return -1;
        if (a.group_order > b.group_order)
            return 1;
        if (a.label < b.label)
            return -1;
        if (a.label > b.label)
            return 1;
        return 0;
    }

    $scope.fetchMenu();
  };