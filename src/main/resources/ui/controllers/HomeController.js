'use strict';

var HomeController = function($rootScope, $scope, $http, $window, $location, $filter, ngTableParams) {

    $rootScope.menuItems = []
    $rootScope.permissions = []
    $rootScope.allowed = {}

    $scope.alerts = []

    $scope.addAlert = function(type, message) {
        $scope.alerts.push({type: type, msg: message});
    };

    $scope.closeAlert = function(index) {
        $scope.alerts.splice(index, 1);
    };

    console.log($rootScope.allowed)
   // console.log($rootScope.permissions)
    $scope.fetch = function() {

        $http.get('/api/timeline')
            .success(function(data){
                $scope.timeline = data;
                console.log(JSON.stringify(data))
            })
            .error(function(response, status, headers, config) {

                $scope.addAlert('danger', 'An error occurred while searching for your transaction');
                if (response)
                    $scope.addAlert('danger', response);
        });
    };

    $scope.fetchMenu = function () {
    
        $http.get('/api/menu/actions')
            .success(function (data, status, headers, config) {
                var items = data.menuItems.sort(comparePermissions);
                $rootScope.permissions = data.permissions

                $rootScope.allowed = data.permissions.reduce(function(map, obj) {
                    map[obj.permission] = true;
                    return map;
                }, {});

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
    $scope.fetch();
};
