'use strict';
function EditUserController($rootScope, $scope, $http, $window, $location, $routeParams) {

    var self = this;
    var userId = $routeParams.id

    $scope.alerts = [];

    $rootScope.showSave = false;
    $rootScope.showReset = false;
    $rootScope.accountingUser = {};

    var permissions = $rootScope.permissions
    for (var i in permissions){
        var p = permissions[i]
        console.log("Checking permission : "+ p.permission)
        if (p.permission == "editUser"){
            console.log("Checking permission found "+ p.permission)
            $rootScope.showSave = true;
            $rootScope.showReset = true;
        }
    };

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


    $scope.save = function(){
        console.log('Save User');
        var accUser = $rootScope.accountingUser;
        var user = {id:accUser.id,
            active:accUser.active,
            username:accUser.username,
            last_name:accUser.last_name,
            first_name:accUser.first_name,
            email:accUser.email,
            role:accUser.role};
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

    $scope.reset = function(){
        console.log('Reset User');
        var user = {};
        $scope.alerts = [];
        $http.post('api/user/reset/'+userId)
            .success(function(data, status, headers, config){
                $scope.addAlert('success', 'User login has been reset. An email will be sent to them with their new password');
            })
            .
            error(function(data, status, headers, config) {
                $scope.addAlert('danger', 'User login not reset');
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
