package com.javaleo.systems.botmaker.ejb.business;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Local;

import com.javaleo.systems.botmaker.ejb.entities.SnippetCode;
import com.javaleo.systems.botmaker.ejb.filters.SnippetCodeFilter;

@Local
public interface ISnippetCodeBusiness extends Serializable {

	void saveSnippetCode(SnippetCode snippetCode);
	
	List<SnippetCode> searchSnippetCodeByFilter(SnippetCodeFilter filter);
	
}
