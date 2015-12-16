package com.liquidforte.launcher.ivy;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Collection;
import java.util.Date;

import org.apache.ivy.Ivy;
import org.apache.ivy.core.module.descriptor.DefaultModuleDescriptor;
import org.apache.ivy.core.module.descriptor.ModuleDescriptor;
import org.apache.ivy.core.module.id.ModuleRevisionId;
import org.apache.ivy.core.report.ResolveReport;
import org.apache.ivy.core.resolve.ResolveOptions;
import org.apache.ivy.core.retrieve.RetrieveOptions;
import org.apache.ivy.core.retrieve.RetrieveReport;
import org.apache.ivy.core.settings.IvySettings;
import org.apache.ivy.plugins.resolver.ChainResolver;

import com.liquidforte.launcher.Artifact;
import com.liquidforte.launcher.Repository;

public class MultiInvoker {
	private IvySettings settings;
	private Ivy ivy;

	private ResolveOptions resolveOptions;
	private ResolveReport[] resolveReport;
	private RetrieveOptions retrieveOptions;
	private RetrieveReport[] retrieveReport;

	public MultiInvoker(Repository[] repository) {
		init(repository);
	}

	public void init(Repository[] repository) {
		settings = new IvySettings();
		settings.setDefaultCache(new File("cache").getAbsoluteFile());

		ChainResolver chain = new ChainResolver();
		chain.setName("Default Chain Resolver");

		for (Repository repo : repository) {
			chain.add(repo.getResolver());
		}

		settings.addResolver(chain);
		settings.setDefaultResolver("Default Chain Resolver");

		ivy = Ivy.newInstance(settings);
	}

	public void resolve(ModuleDescriptor[] descriptor) {
		resolveOptions = new ResolveOptions();
		resolveOptions.setConfs(new String[] {"default"});
		resolveReport = new ResolveReport[descriptor.length];

		try {
			for (int count = 0; count < descriptor.length; count++) {
				resolveReport[count] = ivy.resolve(descriptor[count].getModuleRevisionId(), resolveOptions, false);
			}
		} catch (ParseException | IOException e) {
			e.printStackTrace();
		}
	}

	public void retrieve() {
		retrieveReport = new RetrieveReport[resolveReport.length];
		for (int count = 0; count < resolveReport.length; count++) {
			retrieveOptions = new RetrieveOptions();
			retrieveOptions.setDestArtifactPattern("repo/[orgPath]/[module]/[revision]/[module]-[revision].[ext]");
			retrieveOptions.setResolveId(resolveReport[count].getResolveId());
			try {
				retrieveReport[count] = ivy.retrieve(
						resolveReport[count].getModuleDescriptor().getResolvedModuleRevisionId(), retrieveOptions);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("unchecked")
	public String getClasspath() {
		String classpath = "";

		for (RetrieveReport retrieve : retrieveReport) {
			for (File file : (Collection<File>) retrieve.getRetrievedFiles()) {
				classpath += file.getAbsolutePath() + File.pathSeparator;
			}
		}

		if (classpath.endsWith(File.pathSeparator))
			classpath = classpath.substring(0, classpath.lastIndexOf(File.pathSeparator));

		return classpath;
	}

	public void invoke(Artifact[] artifacts) {
		ModuleDescriptor[] descriptor = new ModuleDescriptor[artifacts.length];

		for (int count = 0; count < descriptor.length; count++) {
			descriptor[count] = new DefaultModuleDescriptor(
					ModuleRevisionId.newInstance(artifacts[count].getOrganization(), artifacts[count].getModule(),
							artifacts[count].getVersion()),
					"integration", new Date());
		}

		resolve(descriptor);
		retrieve();
	}
}
