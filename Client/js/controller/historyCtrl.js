//The controller for the history.html page
app.controller("historyCtrl", function ($rootScope, $scope, $window, $http, baseUrl)
{
	$scope.logout = function()
	{
		$rootScope.user = undefined;
		$rootScope.$broadcast("userChange", $rootScope.user);
		$window.location.href = "#/";
	};

	$scope.showDetails = function(item)
	{
		$rootScope.productDetail = item;
		$window.location.href = '#/product';
	}
	
	
	$scope.getData = function()
	{
		if($rootScope.productHistory !== undefined)
		{
		    //GET /usr/{token}/watchedProducts/{productId}
			$http.get(baseUrl + "/usr/" + $rootScope.user + "/watchedProducts/" + $scope.productHistory.product_id)
			.success(function (data)
			{	
				$scope.result = data;
				console.log("Success!!");
				console.log(data);
				
			})
			.error(function (error, status)
			{
				$scope.sendError = status;
				console.log(error);
				console.log(status);
				console.log("Error!!");
			});
		}
	}
});
