package com.liquidforte.launcher;

public class LaunchDescriptor {
	private Repository[] repositories;
	private Artifact[] artifacts;
	private String mainClass;
	private String[] arguments;

	public LaunchDescriptor(Repository[] repositories, Artifact[] artifacts, String mainClass, String[] arguments) {
		this.repositories = repositories;
		this.artifacts = artifacts;
		this.mainClass = mainClass;
		this.arguments = arguments;
	}

	public String[] getArguments() {
		return arguments;
	}

	public void setArguments(String[] arguments) {
		this.arguments = arguments;
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
