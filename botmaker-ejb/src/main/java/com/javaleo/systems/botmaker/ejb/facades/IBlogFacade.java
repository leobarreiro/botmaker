package com.javaleo.systems.botmaker.ejb.facades;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Local;

import com.javaleo.systems.botmaker.ejb.entities.Page;

@Local
public interface IBlogFacade extends Serializable {

	List<Page> listPagesFromBlog();

}
