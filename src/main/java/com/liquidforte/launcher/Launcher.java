package com.liquidforte.launcher;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.liquidforte.launcher.bootstrap.Bootstrap;
import com.liquidforte.launcher.ivy.MultiInvoker;

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
		MultiInvoker invoker = new MultiInvoker(descriptor.getRepositories());
		invoker.invoke(descriptor.getArtifacts());
		
		String classpath = invoker.getClasspath();
		List<String> command = new ArrayList<String>(Arrays.asList("java", "-cp", classpath, descriptor.getMainClass()));
		command.addAll(Arrays.asList(descriptor.getArguments()));
		
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
