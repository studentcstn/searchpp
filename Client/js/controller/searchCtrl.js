app.controller("searchCtrl", function ($rootScope, $scope, $window, $location, $http, baseUrl)
{	
	$scope.isSending = false;
	if($rootScope.productList !== undefined)
		$scope.result = $rootScope.productList;
	
	var params = $location.search();
	if("token" in params)
	{
		$rootScope.user = params.token;
		$rootScope.$broadcast("userChange", $rootScope.user);
	}
	
	$scope.login = function()
	{
		$window.location.href = baseUrl + "/usr";			
	}

    $scope.logout = function()
    {
        $rootScope.user = undefined;
        $rootScope.$broadcast("userChange", $rootScope.user);
        $window.location.href = "#/";
    };
	
	$scope.search = function(product)
	{
		if($scope.productSearch.$valid)
		{
			var request = {
				search_text: product.name
			};
			
			if("min" in product)
				request.price_min = product.min*100;
			if("max" in product)
				request.price_max = product.max*100;
			if("used" in product)
				request.used = product.used;
			$rootScope.request = request;
			
			$scope.isSending = true;

			$http.get(baseUrl + "/products" , {params : request})
			.success(function (data)
			{	
				$rootScope.productList = $scope.result = data;
				console.log("Result!!");
				console.log(data);
				console.log($scope.result);
				$scope.isSending = false;
				
			})
			.error(function (error, status)
			{
				console.log(error);
				console.log(status);
				console.log("Error!!");
				$scope.isSending = false;
			});
		}
	}
	
	$scope.showDetails = function(item)
	{
		console.log(item);
			$rootScope.productDetail = item;
			$window.location.href = '#/product';
	}
});
