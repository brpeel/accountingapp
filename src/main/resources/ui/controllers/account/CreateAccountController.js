'use strict';
function CreateAccountControler($rootScope, $scope, $http, $window, $location) {

    var self = this;


    $scope.subcategories= []

    $scope.cancel = function(){
        $location.path("/transactions");
    };

    $scope.save = function(){
        console.log('Create Account');
        var data = {
            'name':$scope.name,
            'initialBalance':$scope.initialBalance,
            'normalSide':$scope.side,
            'category':$scope.category,
            'subcategory':$scope.subcategory
        };

        $http.post('api/account',data)
            .success(function(data, status, headers, config){

                console.log(JSON.stringify(data))
                $location.path("/account")
            })
            .
            error(function(data, status, headers, config) {
                console.log(JSON.stringify(data))
                alert("Could not save Account");
            });
    }

    $scope.updateSubCat = function() {
        var category = $scope.category
        if (category)
            category = category.toLowerCase();

        var subcat = []
        if (category == 'asset')
            subcat = ['Cash', 'Current Asset', 'Long Term Asset','Intangible Asset', 'Inventory'];
        else if (category == 'liability')
            subcat = ['Current Liability','Long Term Liabilitiy']
        else if (category == 'owner equity')
            subcat = ['Owner Capital','Owner Drawing']
        else if (category == 'expense')
            subcat = ['Operating Expense','Other Expense']
        else if (category == 'revenue')
            subcat = ['Revenue','Other Income','Revenue - Contra']

        console.log(JSON.stringify(subcat));
        $scope.subcategories = subcat

    }
};
//name, initial_balance, normal_side, subcategory