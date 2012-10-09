package br.com.aexo.sim.core.faces;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewParameter;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewDeclarationLanguage;
import javax.faces.view.ViewMetadata;
import javax.inject.Named;

@Named
@ApplicationScoped
public class Nav {
	public static class Page {
		private final String viewId;
		private final Map<String, String> params;

		private Page(String viewId) {
			this.viewId = viewId;
			this.params = new LinkedHashMap<String, String>();
		}

		private Page(String viewId, Map<String, String> params) {
			this.viewId = viewId;
			this.params = params;
		}

		public Page redirect() {
			return includeParam("faces-redirect", "true");
		}

		public Page includeViewParams() {

			// Getting the metadata facet of the view
			FacesContext ctx = FacesContext.getCurrentInstance();
			Collection<UIViewParameter> viewParameters = ViewMetadata.getViewParameters(ctx.getViewRoot());
			// Looking for a view parameter with the specified name
			for (UIViewParameter child : viewParameters) {
				String name = child.getName();
				String value = child.getStringValue(ctx);
				params.put(name, value);
			}
			return this;
		}

		public Page includeViewParam(String name) {
			// Getting the metadata facet of the view
			FacesContext ctx = FacesContext.getCurrentInstance();
			ViewDeclarationLanguage vdl = ctx.getApplication().getViewHandler().getViewDeclarationLanguage(ctx, viewId);
			ViewMetadata viewMetadata = vdl.getViewMetadata(ctx, viewId);
			UIViewRoot viewRoot = viewMetadata.createMetadataView(ctx);
			UIComponent metadataFacet = viewRoot.getFacet(UIViewRoot.METADATA_FACET_NAME);

			// Looking for a view parameter with the specified name
			UIViewParameter viewParam = null;
			for (UIComponent child : metadataFacet.getChildren()) {
				if (child instanceof UIViewParameter) {
					UIViewParameter tempViewParam = (UIViewParameter) child;
					if (name.equals(tempViewParam.getName())) {
						viewParam = tempViewParam;
						break;
					}
				}
			}

			if (viewParam == null) {
				throw new FacesException("Unknown parameter: '" + name + "' for view: " + viewId);
			}

			// Getting the value
			String value = viewParam.getStringValue(ctx);
			return includeParam(name, value);
		}

		public Page includeParam(String name, String value) {
			Map<String, String> newParams = new LinkedHashMap<String, String>(params);
			newParams.put(name, value);
			return new Page(viewId, newParams);
		}

		public String s() {
			StringBuilder sb = new StringBuilder();
			sb.append(viewId);

			String paramSeparator = "?";
			for (Map.Entry<String, String> nameValue : params.entrySet()) {
				sb.append(paramSeparator).append(nameValue.getKey()).append("=").append(nameValue.getValue() == null ? "" : nameValue.getValue());
				paramSeparator = "&";
			}

			return sb.toString();
		}

		public String getS() {
			return s();
		}
	}

	private final Page novaObservacao = new Page("/admin/servicos/novaobservacao.xhtml");
	private final Page editaServico = new Page("/admin/servicos/servicoedita.xhtml");
	private final Page removeServico = new Page("/admin/servicos/servicoremove.xhtml");
	private final Page detalheServico = new Page("/admin/servicos/servicodetalhe.xhtml");
	private final Page listagemServicos = new Page("/admin/servicos/servicos.xhtml");
	private final Page home = new Page("/admin/home.xhtml");
	private final Page thisPage = new Page("");

	public Page getNovaObservacao() {
		return novaObservacao;
	}

	public Page getDetalheServico() {
		return detalheServico;
	}

	public Page getRemoveServico() {
		return removeServico;
	}

	public Page getEditaServico() {
		return editaServico;
	}

	public Page getListagemServicos() {
		return listagemServicos;
	}

	public Page getHome() {
		return home;
	}

	public Page getThisPage() {
		return thisPage;
	}

}