'use strict';
function LogonController($rootScope, $scope, $http, $window, $location) {

    var self = this;

    $rootScope.errormessage = null;
    $rootScope.menuItems = null;
    $rootScope.permissions = null;

    $scope.user = {};

    $scope.setToken = function(token){
        $window.sessionStorage.token = token
        $rootScope.showLogon = false
    };

    $scope.logon = function(){
        $scope.setToken(null)

        var username = $scope.username;
        var password = $scope.password;

        console.log("Submitting logon "+username);
        var userIn = {
            username : username,
            password : password
        };

        $http.post('auth/authenticate',userIn)
            .success(function(data, status, headers, config){

                $rootScope.errormessage = null;
                $rootScope.username = data.username;
                $rootScope.permissions = data.permissions;
                $rootScope.menuItems = data.mainMenu;
                $scope.user.header = btoa(userIn.username + ':' + userIn.password);
                $scope.setToken(data.token);

                if (data.reset_on_logon || data.password_expired)
                    $location.path("/resetpassword")
                else
                    $location.path("/")
            })
            .
            error(function(data, status, headers, config) {

                $scope.setToken(null);
                $rootScope.errormessage = data;
            });
    };

    $scope.resetPassword = function(){
        console.log('Reset Password');
        var username = $scope.username;

        $http.post('auth/reset',{username: username})
            .success(function(data, status, headers, config){

                // scope.user.username = userIn.username;
               alert("Your password reset email has been sent");

            })
            .
            error(function(data, status, headers, config) {

                alert("Could not send password reset email");
                $rootScope.errormessage = data
            });
    }
};

