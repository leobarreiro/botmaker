package org.javaleo.grandpa.web.action.price;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.javaleo.grandpa.ejb.entities.price.Product;
import org.javaleo.grandpa.ejb.exceptions.BusinessException;
import org.javaleo.grandpa.ejb.facades.IPriceFacade;
import org.javaleo.grandpa.web.action.AbstractCrudSinglePage;
import org.javaleo.grandpa.web.action.MsgAction;

@Named
@ConversationScoped
public class ProductAction extends AbstractCrudSinglePage<Product> implements Serializable {

	private static final long serialVersionUID = 6438597232356923660L;

	private static final String THIS_PAGE = "/pages/price/products.bot?faces-redirect=true";

	@Inject
	private Conversation conversation;

	@Inject
	private IPriceFacade facade;

	@Inject
	private MsgAction msgAction;

	private Product product;
	private List<Product> productList;

	@Override
	public Conversation getConversation() {
		return conversation;
	}

	@Override
	public String loadPage() {
		startConversation();
		startNew();
		searchOnlyActives();
		return THIS_PAGE;
	}

	@Override
	public void startNew() {
		this.product = new Product();
	}

	@Override
	public void searchAll() {
		this.productList = facade.listAllProducts();
	}

	@Override
	public void searchOnlyActives() {
		this.productList = facade.listAllActiveProducts();
	}

	@Override
	public void viewDetails(Product pojo) {
		this.product = pojo;
	}

	@Override
	public void startEdit(Product pojo) {
		this.product = pojo;
	}

	@Override
	public void deactivate(Product pojo) {
		this.product = pojo;
		try {
			facade.deactivateProduct(product);
		} catch (BusinessException e) {
			msgAction.addErrorMessage(e.getMessage());
		}
		searchOnlyActives();
	}

	@Override
	public void save() {
		try {
			facade.saveProduct(product);
		} catch (BusinessException e) {
			msgAction.addErrorMessage(e.getMessage());
		}
		searchOnlyActives();
	}

	// Getters and Setters

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public List<Product> getProductList() {
		return productList;
	}

	public void setProductList(List<Product> productList) {
		this.productList = productList;
	}

}
