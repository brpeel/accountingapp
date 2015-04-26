'use strict';
function MyUserController($rootScope, $scope, $http, $window, $location, $routeParams) {

    var self = this;
    var userId = $routeParams.id

    $scope.alerts = [];

    $rootScope.accountingUser = {};

    $scope.fetchRoles = function(){
        $http.get('/api/user/roles').success(function(data){
            console.log('Roles = '+JSON.stringify(data))
            $rootScope.roles = data;
        });
    };

    $scope.fetch = function() {

        if (typeof userId == "undefined" || !userId) {
            console.log("No userid")
            $location.path("/")
            return;
        }

        console.log('Fetching User with id :'+userId)
        $http.get('/api/user/'+userId)
            .success(function(data){
                console.log('User = '+JSON.stringify(data))
                $rootScope.accountingUser = data;
            })
            .error(function(data, status, headers, config) {
                $scope.addAlert('danger', 'Could not load user data');
                if (typeof data != "undefined" && data && data != "")
                    $scope.addAlert('danger', data);
            });
    };

    $scope.cancel = function(){
        $location.path("/");
    };

    $scope.save = function(){
        console.log('Save User');
        var accUser = $rootScope.accountingUser;
        var user = {id:accUser.id,
            last_name:accUser.last_name,
            first_name:accUser.first_name,
            email:accUser.email};
        $scope.alerts = [];
        $http.put('api/user/update/'+userId,user)
            .success(function(data, status, headers, config){
                $scope.addAlert('success', 'User Saved!');
            })
            .
            error(function(data, status, headers, config) {
                $scope.addAlert('danger', 'User not saved');
                if (typeof data != "undefined" && data && data != "")
                    $scope.addAlert('danger', data);
            });
    };

    $scope.addAlert = function(type, message) {
        console.log("Adding alert "+type+" with message "+message);
        $scope.alerts.push({type: type, msg: message});
    };

    $scope.closeAlert = function(index) {
        $scope.alerts.splice(index, 1);
    };

    $scope.fetchRoles();
    $scope.fetch();
};
