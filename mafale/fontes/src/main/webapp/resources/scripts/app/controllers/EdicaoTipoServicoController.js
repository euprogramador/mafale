function EdicaoTipoServicoController($scope,TiposServico, $routeParams,$location){

	$scope.tipo = {};
	
	if ($routeParams.id!='novo'){
		showModal();
		TiposServico.get({id:$routeParams.id},function(data){
			$scope.tipo = data;
			hideModal();
		},errorHandler);
	}
		
	$scope.salvar = function(){
		showModal();
		TiposServico.save({entidade:$scope.tipo},function(){
			hideModal();
			alert("Tipo de serviço salvo com sucesso");
			$location.path('/listagemTipoServico/1');
		},errorHandler);
	};
}