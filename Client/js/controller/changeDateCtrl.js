app.controller("changeDateCtrl", function ($rootScope, $scope, $window, $http, baseUrl)
{
	$scope.logout = function()
	{
		$rootScope.user = undefined;
		$rootScope.$broadcast("userChange", $rootScope.user);
		$window.location.href = baseUrl + "#/";
	};

	$scope.showDetails = function(item)
	{
		$rootScope.productDetail = item;
		$window.location.href = '#/product';
	}

	$scope.changeDate = function(param)
	{
		if($rootScope.user == undefined)
		{
			$window.location.href = baseUrl + "/usr";
			return;
		}
			
		if(param.date_to !== undefined && $scope.productSetDate !== undefined)
		{
			var request = {
				date_to: param.date_to
			};

			if("date_from" in param)
				request.date_from = param.date_from
			
			$scope.isSending = true;
			
			$http.put(baseUrl + "/usr/" + $rootScope.user + "/watchedProducts/" + $scope.productSetDate.product_id, request)
			.success(function ()
			{	
				console.log("Success!!");	
				$window.location.href = "#/usr/watchedProducts";				
			})
			.error(function (error, status)
			{
				console.log(error);
				console.log(status);
				console.log("Error!!");
			});
		}
	}
	
	$scope.getData = function()
	{
		if($rootScope.productSetDate !== undefined)
			$scope.productSetDate = $rootScope.productSetDate;
	}
});
