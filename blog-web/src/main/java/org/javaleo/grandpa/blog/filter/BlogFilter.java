package org.javaleo.grandpa.blog.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BlogFilter implements Filter {

	private static final String FACES_REQUEST = "Faces-Request";
	private static final String PARTIAL_AJAX = "partial/ajax";
	private static final String UTF_8 = "UTF-8";
	private static final String TEXT_XML = "text/xml";
	private static final Logger LOG = LoggerFactory.getLogger(BlogFilter.class);
	private static final String AJAX_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><partial-response><redirect url=\"%s\"></redirect></partial-response>";

	private FilterConfig filterConfig;

	@Override
	public void init(FilterConfig filterconfig) throws ServletException {
		this.filterConfig = filterconfig;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		try {
			filterChain.doFilter(request, response);
		} catch (Exception e) {
			LOG.error(e.getMessage());
			goToError500(httpResponse);
		}
	}

	@Override
	public void destroy() {

	}

	private void goToError500(HttpServletResponse httpResponse) throws IOException {
		ServletContext servletContext = filterConfig.getServletContext();
		String error500 = servletContext.getContextPath() + servletContext.getInitParameter("error.500");
		httpResponse.sendRedirect(error500);
	}

	@SuppressWarnings("unused")
	private void goToError404(HttpServletResponse httpResponse) throws IOException {
		ServletContext servletContext = filterConfig.getServletContext();
		String error404 = servletContext.getContextPath() + servletContext.getInitParameter("error.404");
		httpResponse.sendRedirect(error404);
	}

	private boolean isAjaxRequest(ServletRequest request) {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		return (PARTIAL_AJAX.equals(httpRequest.getHeader(FACES_REQUEST)));
	}

}
