/*Controller für die Übersicht der Sprechstunden eines Studenten (Route: #/Student/Termine)*/
app.controller("watchedProductCtrl", function ($rootScope, $scope, $window, $http, baseUrl)
{	
	$scope.showDetails = function(item)
	{
		$rootScope.productWatchedDetail = item;
		$window.location.href = '#/product';
	}
	
	$scope.getHistory = function(item)
	{
		$rootScope.productHistory = item;
		$window.location.href = '#/history';
	}
	
	$scope.removeList = function(item)
	{		
		$http.delete(baseUrl + "/usr/" + $rootScope.user + "/watchedProducts")
		.success(function ()
		{	
			console.log("Result!!");	
		})
		.error(function (error, status)
		{
			console.log(error);
			console.log(status);
			console.log("Error!!");
		});
	}
	
	$scope.remove = function(item)
	{
		$http.delete(baseUrl + "/usr/" + $rootScope.user + "/watchedProducts/" + item.product_Id)
		.success(function ()
		{	
			console.log("Result!!");	
		})
		.error(function (error, status)
		{
			console.log(error);
			console.log(status);
			console.log("Error!!");
		});
	}
	
	$scope.getData = function()
	{
		if($rootScope.user !== undefined)
		{
			$http.get(baseUrl + "/usr/" + $rootScope.user + "/watchedProducts")
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
