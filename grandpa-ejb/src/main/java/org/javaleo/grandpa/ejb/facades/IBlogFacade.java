package org.javaleo.grandpa.ejb.facades;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Local;

import org.javaleo.grandpa.ejb.entities.Page;

@Local
public interface IBlogFacade extends Serializable {

	List<Page> listPagesFromBlog();

}
