app.controller("historyCtrl", function ($rootScope, $scope, $window, $http, baseUrl)
{
	$scope.logout = function()
	{
		$rootScope.user = undefined;
		$rootScope.$broadcast("userChange", $rootScope.user);
		$window.location.href = baseUrl + "#/";
	};

	$scope.getData = function()
	{
		if($rootScope.productHistory !== undefined)
		{
			$http.get(baseUrl + "/usr/" + $rootScope.user + "/watchedProducts/" + $scope.productHistory.product_id)
			.success(function (data)
			{	
				$scope.result = data;
				console.log("Result!!");
				console.log(data);
				
			})
			.error(function (error, status)
			{
				console.log(error);
				console.log(status);
				console.log("Error!!");
			});
		}
	}
});