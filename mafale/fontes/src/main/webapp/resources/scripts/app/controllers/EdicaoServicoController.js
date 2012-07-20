function EdicaoServicoController($scope,Servicos,Clientes,TiposServico, $routeParams,$location){

	$scope.servico = {};
	$scope.filtro = [{
		alvo:'razaoSocial|like|string'
	}];
	

	// carrega seletor para clientes
	$scope.paginacaoClientes = montarPaginador(function(inicio,numRegistros){
		showModal();
		Clientes.query({'inicio':inicio,'numRegistros':numRegistros,'filtro':montarFiltro($scope.filtro)},function(data){
			$scope.clientes = data;
			$scope.paginacaoClientes.montarPaginacao(data);
			hideModal();
		},errorHandler);		
	});
	 
	if ($routeParams.id=='novo'){
		TiposServico.query({},function(data){
			$scope.tipos = data.listagem;
		},errorHandler);
	}
		
	if ($routeParams.id!='novo'){
		showModal();
		Servicos.get({id:$routeParams.id},function(data){
			$scope.servico = data;
			
			TiposServico.query({},function(listaTipos){
				$scope.tipos = listaTipos.listagem;
				$scope.servico.tipoServico = configureValue('id',$scope.servico.tipoServico,listaTipos.listagem);
			},errorHandler);
		
			hideModal();
		},errorHandler);
	}
	
	$scope.alterarCliente = function(){
		$scope.paginacaoClientes.$primeiro();
		alterarCliente();
	}
	
	$scope.associar = function(cliente){
		$scope.servico.cliente = cliente;
		$('#SelecionadorDeClientes').dialog('close');
	}
	
	$scope.salvar = function(){
		showModal();
		Servicos.save({entidade:$scope.servico},function(){
			hideModal();
			alert("Serviço salvo com sucesso");
			$location.path('/listagemServico/1');
		},errorHandler);
	}; 

}
