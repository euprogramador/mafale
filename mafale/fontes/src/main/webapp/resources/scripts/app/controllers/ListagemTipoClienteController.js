function ListagemTipoClienteController($scope, TiposCliente, $routeParams){
	$scope.paginacao = montarPaginador(function(inicio,numRegistros){
		showModal();
		TiposCliente.query({'inicio':inicio,'numRegistros':numRegistros},function(data){
			$scope.resultado = data;
			$scope.paginacao.montarPaginacao(data);
			hideModal();
		},errorHandler);		
	});
	$routeParams.pagina || 1;
	$scope.paginacao.paginar($routeParams.pagina);

	$scope.remover = function(tipo){
		confirm('Confirma remoção?',function(){
			showModal();
			TiposCliente.remove({id:tipo.id},function(){
				alert('Tipo removido com sucesso');
				hideModal();
				$scope.paginacao.atualizar();
			},errorHandler);
		});
	};
	
} 
