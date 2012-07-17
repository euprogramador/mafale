function EdicaoPorteClienteController($scope,PortesCliente, $routeParams,$location){

	$scope.porte = {};
	
	if ($routeParams.id!='novo'){
		showModal();
		PortesCliente.get({id:$routeParams.id},function(data){
			$scope.porte = data;
			hideModal();
		},errorHandler);
	}
		
	$scope.salvar = function(){
		showModal();
		PortesCliente.save({entidade:$scope.porte},function(){
			hideModal();
			alert("Porte do cliente salvo com sucesso");
			$location.path('/listagemPorteCliente/1');
		},errorHandler);
	};
}