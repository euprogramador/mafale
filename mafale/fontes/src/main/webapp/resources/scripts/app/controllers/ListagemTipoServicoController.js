function ListagemTipoServicoController($scope, TiposServico, $routeParams){
	
	$scope.paginacao = montarPaginador(function(inicio,numRegistros){
		showModal();
		TiposServico.query({'inicio':inicio,'numRegistros':numRegistros},function(data){
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
			TiposServico.remove({id:tipo.id},function(){
				alert('Tipo de serviço removido com sucesso');
				hideModal();
				$scope.paginacao.atualizar();
			},errorHandler);
		});
	};
	
} 
