package com.liquidforte.launcher;

import org.apache.ivy.plugins.resolver.IBiblioResolver;

public class Repository {
	private String name = "";
	private String url = "";
	private transient IBiblioResolver resolver = null;

	public Repository() {

	}

	public Repository(String name, String url) {
		this();
		this.name = name;
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setResolver(IBiblioResolver resolver) {
		this.resolver = resolver;
	}

	public IBiblioResolver getResolver() {
		if (resolver == null) {
			resolver = new IBiblioResolver();
			resolver.setM2compatible(true);
			resolver.setRoot(url);
		}
		return resolver;
	}
}
