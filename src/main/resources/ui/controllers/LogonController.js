'use strict';
function LogonController($rootScope, $scope, $http, $window, $location) {

    var self = this;

    $rootScope.menuItems = [];

    $scope.user = {}

    $scope.setToken = function(token){
        console.log('Token '+JSON.stringify(token));
        $window.sessionStorage.token = token
        $scope.showLogon = false
    }

    $scope.setMenuItems = function(items){
        console.log('Menu '+JSON.stringify(items));
        $rootScope.menuItems = items;
    }

    $scope.logon = function(){
        var username = $scope.username;
        var password = $scope.password;

        console.log("Submitting logon "+username);
        var userIn = {
            username : username,
            password : password
        }

        $http.post('auth/authenticate',userIn)
            .success(function(data, status, headers, config){

               // scope.user.username = userIn.username;
                $scope.user.header = btoa(userIn.username + ':' + userIn.password);
                $scope.setToken(data.token);
                $scope.setMenuItems(data.menuItems);
                $location.path("/")

            })
            .
            error(function(data, status, headers, config) {
                console.log(JSON.stringify(data))
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