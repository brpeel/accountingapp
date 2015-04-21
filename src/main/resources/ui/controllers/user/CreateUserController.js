'use strict';
function CreateUserController($rootScope, $scope, $http, $window, $location) {

    var self = this;

    $scope.alerts = [];
    $rootScope.showSave = false;
    $rootScope.showReset = false;
    $rootScope.accountingUser = {};

    var permissions = $rootScope.permissions
    for (var i in permissions){
        var p = permissions[i]
        if (p.permission == "createUser"){
            $rootScope.showSave = true;
        }
    };

    $scope.fetchRoles = function(){
        $http.get('/api/user/roles').success(function(data){
            console.log('Roles = '+JSON.stringify(data))
            $rootScope.roles = data;
        });
    };

    $scope.save = function(){
        console.log('Create User');
        var accUser = $rootScope.accountingUser;
        var user = {
            active:accUser.active,
            username:accUser.username,
            last_name:accUser.last_name,
            first_name:accUser.first_name,
            email:accUser.email,
            role:accUser.role};

        $http.post('api/user/',user)
            .success(function(data, status, headers, config){
                $scope.addAlert('success', 'User has been created. An email will be sent to them with their username and password');
            })
            .
            error(function(data, status, headers, config) {
                if (typeof data == "undefined" || !data || data == "")
                    $scope.addAlert('danger', "Could not save user");
                else
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
};
