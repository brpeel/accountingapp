'use strict';
function MenuController($scope, $http) {
  $scope.menuItems = [
    {label:'learn angular'},
    {label:'build an angular app'}];

    $scope.fetchAbout = function() {

            $http.get('/mainmenu/actions').success(function(items){

                console.log(items);

                $scope.menuItems = items;
            });
        }

        $scope.fetchAbout();
  };