'use strict';
function LogonController($scope, $http) {
    $scope.user = {}
    $scope.logon = function(){
        console.log("Submitting logon");

        var userIn = {
            username : 'brpeel',
            password : 'password'
        }
        var scope = $scope;

        $http.post('auth/authenticate',userIn)
            .success(function(data, status, headers, config){
                console.log(JSON.stringify(data))

                if (data.success === true) {
                    scope.user.username = userIn.username;
                    scope.user.header = btoa(userIn.username + ':' + userIn.password);
                    ctrl.errorMessage = '';

                } else {
                    // show error and logout user
                }
            })
            .
            error(function(data, status, headers, config) {
                console.log(JSON.stringify(data))
            });;
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