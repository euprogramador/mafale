$(function() {
	
	// configura a aplicação para executar os commands
	History.Adapter.bind(window, 'statechange', function() {
		var state = History.getState();
		limpaSelecaoMenu();
		eval(state.data.op + '()');
	});

	var starter = getParameterByName('op') || 'showDashboard';
	eval(starter + '()');
	History.pushState({'op' : starter}, '', "?op=" + starter);
	
	// ------------------ menu --------------------//
	$("#menu-dashboard").click(executeCommand('showDashboard'));
	$("#menu-cadastroCliente").click(executeCommand('showCadastroCliente'));
});


// ---------------- commands---------------------------//

function showDashboard() {
	limpaSelecaoMenu("#menu-dashboard");
	$("#menu-dashboard").addClass("current");
	loadTemplate('dashboard.html', inContent);
}

function showCadastroCliente() {
	limpaSelecaoMenu("#menu-cadastro");
	$("#menu-cadastro").addClass("current");
	$("#menu-cadastro").next().slideDown("normal");
	
	$("#menu-cadastroCliente").addClass("current");
	loadTemplate('cadastroCliente.html', inContent);
}

function inContent(html) {
	$("#content").empty().append(html);
}

function limpaSelecaoMenu(menu) {
	$("#main-nav .nav-top-item").removeClass("current");
	$("#main-nav li ul li a").removeClass("current");
	$(menu).parent().siblings().find("ul").slideUp("normal"); 
}

/**
 * script para carga de templates
 * 
 * @param template
 * @param handler
 */
function loadTemplate(template, handler) {
	$.ajax({
		url : 'templates/' + template,
		success : function(data) {
			handler(data);
		}
	});
}
/**
 * recupera um parametro via querystring
 * 
 * @param name
 * @returns
 */
function getParameterByName(name) {
	name = name.replace(/[\[]/, "\\\[").replace(/[\]]/, "\\\]");
	var regexS = "[\\?&]" + name + "=([^&#]*)";
	var regex = new RegExp(regexS);
	var results = regex.exec(window.location.search);
	if (results == null)
		return "";
	else
		return decodeURIComponent(results[1].replace(/\+/g, " "));
}
 
/**
 * função usada para retornar uma function configurada para executar um command
 * usado no menu
 * 
 * @param op
 * @returns {Function}
 */
function executeCommand(op) {
	return function() {
		History.pushState({	'op' : op}, '', "?op=" + op);
	};
}
