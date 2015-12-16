package com.liquidforte.launcher;

public class LaunchDescriptor {
	private Repository[] repositories;
	private Artifact[] artifacts;
	private String mainClass;

	public LaunchDescriptor(Repository[] repositories, Artifact[] artifacts, String mainClass) {
		this.repositories = repositories;
		this.artifacts = artifacts;
		this.mainClass = mainClass;
	}

	public Repository[] getRepositories() {
		return repositories;
	}

	public void setRepositories(Repository[] repositories) {
		this.repositories = repositories;
	}

	public Artifact[] getArtifacts() {
		return artifacts;
	}

	public void setArtifacts(Artifact[] artifacts) {
		this.artifacts = artifacts;
	}

	public String getMainClass() {
		return mainClass;
	}

	public void setMainClass(String mainClass) {
		this.mainClass = mainClass;
	}
}
