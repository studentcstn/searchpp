app.controller("productDetailCtrl", function ($rootScope, $scope, $window, $http, baseUrl)
{	
	$scope.toWebsite = function(item)
	{		
		$window.open(item.origin_url, '_blank');
	}

	$scope.addToList = function(param)
	{
		if($rootScope.user == undefined)
		{
			$window.location.href = baseUrl + "/usr";
			return;
		}
			
		if(param.date_to !== undefined && $scope.result !== undefined)
		{
			var request = {
				date_to: param.date_to,
				product_id: $scope.result.product_id
			};

			if("date_from" in param)
				request.date_from = param.date_from
			
			$scope.isSending = true;
			
			$http.post(baseUrl + "/usr/" + $rootScope.user + "/watchedProducts", request)
			.success(function ()
			{	
				console.log("Success!!");					
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
		if($rootScope.productDetail !== undefined)
		{
			console.log($rootScope.productDetail);
			console.log($rootScope.productDetail.product_id);
			if($rootScope.request !== undefined)
			{
				var request = {};
				if ("min" in $rootScope.request)
					request.price_min = $rootScope.request.price_min;
				if ("max" in $rootScope.request)
					request.price_max = $rootScope.request.price_max;
				if ("used" in  $rootScope.request)
					request.used =  $rootScope.request.used;
			}

			$http.get(baseUrl + "/products/" + $rootScope.productDetail.product_id, {params : request})
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
			
			$http.get(baseUrl + "/products/" + $rootScope.productDetail.product_id + "/ratings")
			.success(function (data)
			{	
				$scope.rating = data;
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
