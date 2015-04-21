'use strict';
function EditUserController($rootScope, $scope, $http, $window, $location, $routeParams) {

    var self = this;
    var userId = $routeParams.id

    $scope.errormessage = null;

    $rootScope.showSave = false;
    $rootScope.accountingUser = {};

    var permissions = $rootScope.permissions
    console.log(JSON.stringify(permissions))
    for (var i in permissions){
        var p = permissions[i]
        console.log("Checking permission : "+ p.permission)
        if (p.permission == "editUser"){
            console.log("Checking permission found "+ p.permission)
            $rootScope.showSave = true;
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

                $scope.errormessage = data;
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
            email:accUser.email};

        $http.put('api/user/update/'+userId,user)
            .success(function(data, status, headers, config){

                $location.path("/users")
            })
            .
            error(function(data, status, headers, config) {

                $scope.errormessage = data;
            });
    };

    $scope.reset = function(){
        console.log('Reset User');
        var user = {};

        $http.post('api/user/reset/'+userId)
            .success(function(data, status, headers, config){

                $location.path("/users")
            })
            .
            error(function(data, status, headers, config) {

                $scope.errormessage = data;
            });
    };

    $scope.fetchRoles();
    $scope.fetch();
};
