package com.javaleo.systems.botmaker.ejb.business;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Local;

import com.javaleo.systems.botmaker.ejb.entities.Page;
import com.javaleo.systems.botmaker.ejb.exceptions.BusinessException;
import com.javaleo.systems.botmaker.ejb.filters.PageFilter;

@Local
public interface IPageBusiness extends Serializable {

	void savePage(Page page) throws BusinessException;

	List<Page> listPages(PageFilter pageFilter);

	List<Page> listLastPagesEdited();

	void disablePage(Page page) throws BusinessException;

}
