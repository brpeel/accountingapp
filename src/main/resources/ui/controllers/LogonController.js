'use strict';
function LogonController($rootScope, $scope, $http, $window, $location) {

    var self = this;

    $rootScope.menuItems = [];

    $scope.user = {};
    $scope.showInvalid = false;

    $scope.setToken = function(token){
        $window.sessionStorage.token = token
        $rootScope.showLogon = false
    };

    $scope.setMenuItems = function(items){
        $scope.menuItems = items;
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

               // scope.user.username = userIn.username;
                $scope.user.header = btoa(userIn.username + ':' + userIn.password);
                $scope.setToken(data.token);

                if (data.reset_on_logon)
                    $location.path("/resetpassword")
                else
                    $location.path("/")

            })
            .
            error(function(data, status, headers, config) {
                console.log(JSON.stringify(data))
                $scope.showInvalid = true
                $scope.setToken(null)
                $scope.setMenuItems(null);
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
                console.log(JSON.stringify(data))
                alert("Could not send password reset email");
            });
    }
};

/*
*
* var userIn = {
 username : username,
 password : password
 }
 var that = this;
 $http.post('./login', userIn).success(function(data) {
 if (data.success === true) {
 that.user.username = userIn.username;
 that.user.header = btoa(userIn.username + ':' + userIn.password);
 ctrl.errorMessage = '';
 that.storeToSession();
 } else {
 // show error and logout user
}
}).error(function(arg) {
    // show error and logout user //
});
*
* */