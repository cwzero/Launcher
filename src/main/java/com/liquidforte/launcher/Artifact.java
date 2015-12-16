package com.liquidforte.launcher;

public class Artifact {
	private String organization = "";
	private String module = "";
	private String version = "";

	public Artifact() {
		super();
	}

	public Artifact(String organization, String module, String version) {
		this();
		this.organization = organization;
		this.module = module;
		this.version = version;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
}
