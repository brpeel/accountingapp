	// create the module and name it accountingApp
	//var accountingApp = angular.module('accountingApp', ['ngRoute', 'accountingApp.about']);
	var accountingApp = angular.module('accountingApp', ['ngRoute']);

	// configure our routes
	accountingApp.config(function($routeProvider) {
		$routeProvider

			// route for the home page
			.when('/', {
				templateUrl : 'templates/home.html',
				controller  : 'mainController'
			})

			// route for the about page
			.when('/about', {
				templateUrl : 'templates/about.html',
				controller  : 'aboutController'
			})

	});

	// create the controller and inject Angular's $scope
	accountingApp.controller('mainController', function($scope) {
		// create a message to display in our view
		$scope.message = 'Everyone come and see how good I look!';

	});

	accountingApp.controller('aboutController', function($scope, $http) {

		var controller = new AboutController($scope, $http)

	});
