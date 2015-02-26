'use strict';
function CreateTransController($rootScope, $scope, $http, $window, $location) {

    var self = this;

    $scope.save = function(){
        console.log('Reset Password');
        var trans = {'description':$scope.description};

        $http.post('api/transaction',trans)
            .success(function(data, status, headers, config){

                console.log(JSON.stringify(data))
                $location.path("/transaction")
            })
            .
            error(function(data, status, headers, config) {
                console.log(JSON.stringify(data))
                alert("Could not save transaction");
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