'use strict';
function ResetPasswordController($rootScope, $scope, $http, $window, $location) {

    $rootScope.menuItems = [];
    $rootScope.errormessage = null;

    $scope.reset = function(){

        var password = $scope.password;
        $http.post('api/user/setpassword',{password:password})
            .success(function(data, status, headers, config){
                    $rootScope.errormessage = null;
                    $location.path("/")
            })
            .
            error(function(data, status, headers, config) {
                console.log(JSON.stringify(data))
                $rootScope.errormessage = data
            });
    };
};
