'use strict';
function AssignSurrogateController($rootScope, $scope, $http, $window, $location, $routeParams) {

    console.log(JSON.stringify("In Surrogate Controller"));

    var self = this;

    $rootScope.tForm = {}

    $scope.fetchOptions = function() {
        $http.get('/api/user/all').success(function(data){
            console.log("USers : "+JSON.stringify(data))
            $rootScope.users = data;
        });
    };

    $scope.save = function(){
        console.log('Save Transaction');

        //var surrogate = {userid:$scope.tForm.description, entries:$scope.tForm.entry};

        $http.post('api/user/assignsurrogate',surrogate)
            .success(function(data, status, headers, config){

                $location.path("/home")
            })
            .
            error(function(data, status, headers, config) {
                console.log(data)
                $scope.errormessage = data;
            });
    };


    $scope.toggleMin = function() {
        $scope.minDate = $scope.minDate ? null : new Date();
    };
    $scope.toggleMin();

    $scope.open = function($event) {
        console.log("open ")
        $event.preventDefault();
        $event.stopPropagation();

        $scope.opened = true;
    };

    $scope.dateOptions = {
        formatYear: 'yy',
        startingDay: 1
    };

    $scope.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
    $scope.format = $scope.formats[0];

    $scope.fetchOptions()
};
