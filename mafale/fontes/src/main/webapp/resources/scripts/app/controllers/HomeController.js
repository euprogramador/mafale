function HomeController($scope, TiposCliente){
	$scope.data = 'carlos';
	TiposCliente.list({},emptyFn,function(){console.log(arguments)});
}