package com.javaleo.systems.botmaker.ejb.business;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Local;

import com.javaleo.systems.botmaker.ejb.entities.Snippet;
import com.javaleo.systems.botmaker.ejb.exceptions.BusinessException;
import com.javaleo.systems.botmaker.ejb.filters.SnippetFilter;

@Local
public interface ISnippetBusiness extends Serializable {

	void saveSnippet(Snippet snippetCode) throws BusinessException;

	List<Snippet> searchSnippetByFilter(SnippetFilter filter);

}
