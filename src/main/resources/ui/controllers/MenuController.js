'use strict';
var MenuController = function($rootScope, $scope, $http, $window, $location) {
    $rootScope.menuItems = []
    $rootScope.permissions = []
    $rootScope.allowed = {}

    var isAuthed = function() {
        var token = $window.sessionStorage.token;
        return (typeof token != "undefined" && token);
    };

    $scope.fetchMenu = function () {
        if (!isAuthed())
            return;

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
};