package com.liquidforte.launcher;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.liquidforte.launcher.bootstrap.Bootstrap;
import com.liquidforte.launcher.ivy.IvyInvoker;

public class Launcher {
	public static void main(String[] args) {
		Bootstrap.init();
		String workDir = Bootstrap.getWorkDir();
		File confDir = new File(workDir, "conf");
		if (!confDir.exists() || !confDir.isDirectory()) {
			confDir.mkdirs();
		}
		
		File launcherGson = new File(confDir, "launch.conf");
		if (launcherGson.exists()) {
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			try {
				LaunchDescriptor descriptor = gson.fromJson(new FileReader(launcherGson), LaunchDescriptor.class);
				launch(descriptor);
			} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void launch(LaunchDescriptor descriptor) {
		IvyInvoker invoker = new IvyInvoker(descriptor.getRepositories());
		invoker.invoke(descriptor.getArtifacts());
		
		String classpath = invoker.getClasspath();
		String[] command = {"java", "-cp", classpath, descriptor.getMainClass()};
		
		ProcessBuilder builder = new ProcessBuilder(command);
		builder.inheritIO();
		builder.directory(new File(Bootstrap.getWorkDir()));
		
		try {
			builder.start().waitFor();
		} catch (InterruptedException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
