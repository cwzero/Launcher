package com.liquidforte.launcher.ivy;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Collection;

import org.apache.ivy.Ivy;
import org.apache.ivy.core.module.descriptor.ModuleDescriptor;
import org.apache.ivy.core.report.ResolveReport;
import org.apache.ivy.core.resolve.ResolveOptions;
import org.apache.ivy.core.retrieve.RetrieveOptions;
import org.apache.ivy.core.retrieve.RetrieveReport;
import org.apache.ivy.core.settings.IvySettings;
import org.apache.ivy.plugins.resolver.ChainResolver;
import org.apache.ivy.plugins.resolver.IBiblioResolver;

public class IvyInvoker {
	private IvySettings settings;
	private Ivy ivy;
	
	private ResolveOptions resolveOptions;
	private ResolveReport resolveReport;
	private RetrieveOptions retrieveOptions;
	private RetrieveReport retrieveReport;
	
	public IvyInvoker() {
		init();
	}

	public void init() {
		settings = new IvySettings();
		
		settings.setDefaultCache(new File("cache").getAbsoluteFile());
		
		ChainResolver chain = new ChainResolver();
		chain.setName("Default Chain Resolver");
		IBiblioResolver mavenCentral = new IBiblioResolver();
		mavenCentral.setName("Maven Central");
		mavenCentral.setM2compatible(true);
		mavenCentral.setRoot("http://central.maven.org/maven2/");
		chain.add(mavenCentral);
		settings.addResolver(chain);
		settings.setDefaultResolver("Default Chain Resolver");

		ivy = Ivy.newInstance(settings);
	}
	
	public void resolve(ModuleDescriptor descriptor) {
		resolveOptions = new ResolveOptions();
		resolveOptions.setConfs(new String[] {"default"});

		try {
			resolveReport = ivy.resolve(descriptor, resolveOptions);
		} catch (ParseException | IOException e) {
			e.printStackTrace();
		}
	}

	public void retrieve() {
		retrieveOptions = new RetrieveOptions();
		retrieveOptions.setDestArtifactPattern("repo/[orgPath]/[module]/[revision]/[module]-[revision].[ext]");

		try {
			retrieveReport = ivy.retrieve(resolveReport.getModuleDescriptor().getResolvedModuleRevisionId(), retrieveOptions);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public String getClasspath() {
		String classpath = "";

		for (File file : (Collection<File>) retrieveReport.getRetrievedFiles()) {
			classpath += file.getAbsolutePath() + File.pathSeparator;
		}

		if (classpath.endsWith(File.pathSeparator))
			classpath = classpath.substring(0, classpath.lastIndexOf(File.pathSeparator));
		
		return classpath;
	}
}
