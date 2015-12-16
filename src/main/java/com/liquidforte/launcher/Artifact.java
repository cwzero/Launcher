package com.liquidforte.launcher;

public class Artifact {
	private String organisation = "";
	private String module = "";
	private String version = "";

	public Artifact() {
		super();
	}

	public Artifact(String organisation, String module, String version) {
		this();
		this.organisation = organisation;
		this.module = module;
		this.version = version;
	}

	public String getOrganisation() {
		return organisation;
	}

	public void setOrganisation(String organisation) {
		this.organisation = organisation;
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
