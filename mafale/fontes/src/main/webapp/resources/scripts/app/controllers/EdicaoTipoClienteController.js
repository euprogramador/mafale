function EdicaoTipoClienteController($scope,TiposCliente, $routeParams,$location){

	$scope.tipo = {};
	
	if ($routeParams.id!='novo'){
		showModal();
		TiposCliente.get({id:$routeParams.id},function(data){
			$scope.tipo = data;
			hideModal();
		},errorHandler);
	}
		
	$scope.salvar = function(){
		showModal();
		TiposCliente.save({tipoCliente:$scope.tipo},function(){
			hideModal();
			alert("Tipo salvo com sucesso");
			//$location.path('/listagemTipoCliente/1');
			$("#menu-visualizarTipoCliente").click();
		},errorHandler);
	};
}