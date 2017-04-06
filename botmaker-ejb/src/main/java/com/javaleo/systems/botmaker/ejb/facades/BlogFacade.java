package com.javaleo.systems.botmaker.ejb.facades;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;

import com.javaleo.systems.botmaker.ejb.business.IPageBusiness;
import com.javaleo.systems.botmaker.ejb.entities.Page;

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
