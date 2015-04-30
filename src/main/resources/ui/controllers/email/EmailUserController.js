'use strict';
function EmailUserController($rootScope, $scope, $http, $window, $location) {

    var self = this;

    $scope.alerts = [];
    $scope.users= [];
    $scope.disable = false;

    $scope.fetchUsers = function(){
        $http.get('/api/user/all').success(function(data){
            console.log(JSON.stringify(data))
            $scope.users = data;
        });
    };

    $scope.cancel = function(){
        $location.path("/users");
    };

    $scope.send = function(){
        $scope.alerts = [];

        $scope.addAlert('warning', 'Attempting to send the email. This could take several seconds');

        $scope.disable = true;

        var to = $scope.userid;
        var email = {
            subject:$scope.subject,
            body:$scope.body};

        $http.post('api/email/simple/'+to,email)
            .success(function(data, status, headers, config){
                $scope.disable = false;
                $scope.alerts = [];
                $scope.addAlert('success', 'Email has been sent!');
            })
            .
            error(function(data, status, headers, config) {
                    $scope.disable = false;
                $scope.alerts = [];
                if (typeof data == "undefined" || !data || data == "")
                    $scope.addAlert('danger', "Could not email user");
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

    $scope.fetchUsers();
};
