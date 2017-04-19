package org.javaleo.grandpa.ejb.facades;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;

import org.javaleo.grandpa.ejb.business.IPageBusiness;
import org.javaleo.grandpa.ejb.entities.Page;

@Named
@Stateless
public class BlogFacade implements IBlogFacade {

	private static final long serialVersionUID = 1L;

	@Inject
	private IPageBusiness pageBusiness;

	@Override
	public List<Page> listPagesFromBlog() {
		return pageBusiness.listPagesFromBlog();
	}

}
