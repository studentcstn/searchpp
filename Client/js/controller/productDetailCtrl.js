/*Controller für die Übersicht der Sprechstunden eines Studenten (Route: #/Student/Termine)*/
app.controller("productDetailCtrl", function ($rootScope, $scope, $window, $http, baseUrl)
{	
	//$scope.result = {"data":[{"origin_url":"https:\/\/www.amazon.de\/Raspberry-Pi-Model-ARM-Cortex-A53-Bluetooth\/dp\/B01CD5VC92?psc=1&SubscriptionId=AKIAJWTGO22HIACK52WQ&tag=searchpp06-21&linkCode=xm2&camp=2025&creative=165953&creativeASIN=B01CD5VC92","price":33.0,"name":"Raspberry Pi 3 Model B ARM-Cortex-A53 4x 1,2GHz, 1GB RAM, WLAN, Bluetooth, LAN, 4x USB","rating":4.7,"origin_id":"B01CD5VC92","type":"NEW"},{"origin_url":"http:\/\/www.ebay.de\/itm\/Raspberry-Pi-3-Model-B-ARM-Cortex-A53-4x-1-2GHz-1GB-RAM-WLAN-Bluetooth-LAN-\/401462932066","price":34.49,"name":"Raspberry Pi 3 Model B ARM-Cortex-A53 4x 1,2GHz, 1GB RAM, WLAN, Bluetooth, LAN, ","origin_id":"401462932066","type":"NEW_OTHER"},{"origin_url":"http:\/\/www.ebay.de\/itm\/Raspberry-Pi-3-Modell-B-Board-1-GB-RAM-Quad-Core-64Bit-Bluetooth-Wifi-\/172569593004","price":54.95,"name":"Raspberry Pi 3 Modell B Board 1 GB RAM Quad Core 64Bit Bluetooth Wifi","origin_id":"172569593004","type":"NEW"},{"origin_url":"http:\/\/www.ebay.de\/itm\/Raspberry-Pi-896-8660-All-in-One-Desktop-PC-3-Modell-B-Prozessor-1-2-GHz-Quad-\/322993900079","price":47.34,"name":"Raspberry Pi 896-8660 All-in-One Desktop PC \"3 Modell B Prozessor\" (1,2 GHz Quad","origin_id":"322993900079","type":"NEW"},{"origin_url":"http:\/\/www.ebay.de\/itm\/Raspberry-Pi-3-Model-B-ARM-Cortex-A53-4x-1-2GHz-1GB-RAM-WLAN-Bluetooth-LAN-\/112750560705","price":53.75,"name":"Raspberry Pi 3 Model B ARM-Cortex-A53 4x 1,2GHz, 1GB RAM, WLAN, Bluetooth, LAN,","origin_id":"112750560705","type":"NEW"},{"origin_url":"http:\/\/www.ebay.de\/itm\/Raspberry-Pi-3-Model-B-Quad-Core-CPU-1-2-GHz-1-GB-RAM-Motherboard-4-x-USB-Ports-\/122780043446","price":38.75,"name":"Raspberry Pi 3 Model B Quad Core CPU 1.2 GHz 1 GB RAM Motherboard 4 x USB Ports ","origin_id":"122780043446","type":"NEW"},{"origin_url":"http:\/\/www.ebay.de\/itm\/Raspberry-Pi-3-Model-B-ARM-Cortex-A53-4x-1-2GHz-1GB-RAM-WLAN-Bluetooth-LAN-\/401469818151","price":33.7,"name":"Raspberry Pi 3 Model B ARM-Cortex-A53 4x 1,2GHz, 1GB RAM, WLAN, Bluetooth, LAN, ","origin_id":"401469818151","type":"MANUFACTURER_REFURBISHED"},{"origin_url":"http:\/\/www.ebay.de\/itm\/Raspberry-Pi-896-8660-All-in-One-Desktop-PC-3-Modell-B-Prozessor-1-2-GHz-Quad-\/382299557742","price":47.15,"name":"Raspberry Pi 896-8660 All-in-One Desktop PC \"3 Modell B Prozessor\" (1,2 GHz Quad","origin_id":"382299557742","type":"NEW"},{"origin_url":"http:\/\/www.ebay.de\/itm\/Raspberry-Pi-896-8660-All-in-One-Desktop-PC-3-Modell-B-Prozessor-1-2-GHz-Quad-\/202175506514","price":49.5,"name":"Raspberry Pi 896-8660 All-in-One Desktop PC \"3 Modell B Prozessor\" (1,2 GHz Quad","origin_id":"202175506514","type":"NEW"},{"origin_url":"http:\/\/www.ebay.de\/itm\/Raspberry-Pi-896-8660-All-in-One-Desktop-PC-3-Modell-B-Prozessor-1-2-GHz-Quad-\/292305043757","price":45.38,"name":"Raspberry Pi 896-8660 All-in-One Desktop PC \"3 Modell B Prozessor\" (1,2 GHz Quad","origin_id":"292305043757","type":"NEW"},{"origin_url":"http:\/\/www.ebay.de\/itm\/Raspberry-Pi-896-8660-All-in-One-Desktop-PC-3-Modell-B-Prozessor-1-2-GHz-Quad-\/142644597539","price":49.88,"name":"Raspberry Pi 896-8660 All-in-One Desktop PC \"3 Modell B Prozessor\" (1,2 GHz Quad","origin_id":"142644597539","type":"NEW"},{"origin_url":"http:\/\/www.ebay.de\/itm\/Raspberry-Pi-896-8660-All-in-One-Desktop-PC-3-Modell-B-Prozessor-1-2-GHz-Quad-\/132411265132","price":60.77,"name":"Raspberry Pi 896-8660 All-in-One Desktop PC \"3 Modell B Prozessor\" (1,2 GHz Quad","origin_id":"132411265132","type":"NEW"}],"product_id":4251102611243,"elements":12};
	console.log($scope.result);
	
	$scope.toWebsite = function(item)
	{		
		$window.open(item.origin_url, '_blank');
	}

	$scope.addToList = function(item)
	{		
		$window.open(item.origin_url, '_blank');
	}
	
	if($rootScope.productDetail !== undefined)
	{
		console.log($rootScope.productDetail);
		console.log($rootScope.productDetail.product_id);
		$http.get(baseUrl + "/products/" + $rootScope.productDetail.product_id)
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
			console.log("Error!");
		});
	}
});
