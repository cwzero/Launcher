package com.liquidforte.launcher.bootstrap;

import java.io.File;
import java.io.IOException;

public class Bootstrap {
	private static File workDir;
	private static File libDir;
	private static File cacheDir;
	private static File repoDir;
	private static File confDir;

	private static final String mavenCentral = "http://central.maven.org/maven2/";
	private static final String ivyOrg = "org.apache.ivy";
	private static final String ivyName = "ivy";
	private static final String ivyRev = "2.4.0";
	
	private static final String gsonOrg = "com.google.code.gson";
	private static final String gsonName = "gson";
	private static final String gsonRev = "2.5";
	
	private static String buildRepo;
	private static final String launcherOrg = "com.liquidforte";
	private static final String launcherName = "Launcher";
	private static final String launcherRev = "0.0.0";
	
	private static String classpath = "";
	
	public static String getWorkDir() {
		return workDir.getAbsolutePath();
	}
	
	public static void init() {
		libDir = new File("lib").getAbsoluteFile();
		if (!libDir.exists() || !libDir.isDirectory()) {
			libDir.mkdirs();
		}
		
		workDir = libDir.getParentFile();
		buildRepo = "file:///" + new File(new File(workDir.getParentFile(), "build"), "maven").getAbsolutePath() + "/";
		
		cacheDir = new File(workDir, "cache");
		if (!cacheDir.exists() || !cacheDir.isDirectory()) {
			cacheDir.mkdirs();
		}
		
		repoDir = new File(workDir, "repo");
		if (!repoDir.exists() || !repoDir.isDirectory()) {
			repoDir.mkdirs();
		}
		
		confDir = new File(workDir, "conf");
		if (!confDir.exists() || !confDir.isDirectory()) {
			confDir.mkdirs();
		}}
	
	public static void bootstrap() {
		classpath += new ArtifactDownloader(ivyOrg, ivyName, ivyRev).download(mavenCentral, libDir.getAbsolutePath()) + File.pathSeparator;
		classpath += new ArtifactDownloader(gsonOrg, gsonName, gsonRev).download(mavenCentral, libDir.getAbsolutePath()) + File.pathSeparator;
		classpath += new ArtifactDownloader(launcherOrg, launcherName, launcherRev).download(buildRepo, libDir.getAbsolutePath());
		
		ProcessBuilder builder = new ProcessBuilder("java", "-cp", classpath, "com.liquidforte.launcher.Launcher");
		builder.directory(workDir);
		builder.inheritIO();
		try {
			builder.start().waitFor();
		} catch (InterruptedException | IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Bootstrap.init();
		Bootstrap.bootstrap();
	}
}
