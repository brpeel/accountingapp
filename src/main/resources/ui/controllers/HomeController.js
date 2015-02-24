'use strict';
function MenuController($scope, $http) {
  $scope.menuItems = [];

    $scope.fetchAbout = function() {

            $http.get('/api/mainmenu/actions').success(function(items){

                $scope.menuItems = items;
            });
        }

        $scope.fetchAbout();
  };