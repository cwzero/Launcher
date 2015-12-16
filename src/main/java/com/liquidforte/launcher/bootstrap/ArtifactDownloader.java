package com.liquidforte.launcher.bootstrap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class ArtifactDownloader {
	private String path = "";

	public ArtifactDownloader(String org, String name, String rev) {
		path = org.replace(".", "/") + "/" + name + "/" + rev + "/" + name + "-" + rev + ".jar";
	}
	
	public String download(String repoUrl, String parentDir) {
		String fullUrl = "";
		fullUrl = repoUrl;
		if (!fullUrl.endsWith("/")) {
			fullUrl += "/";
		}
		
		fullUrl += path;
		
		File parent = new File(parentDir);
		if (!parent.exists() || !parent.isDirectory()) {
			parent.mkdirs();
		}
		
		File dest = new File(parent, fullUrl.substring(fullUrl.lastIndexOf('/')).replace("/", ""));
		
		URL source = null;
		try {
			source = new URL(fullUrl);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		ReadableByteChannel rbc = null;
		try {
			rbc = Channels.newChannel(source.openStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		FileOutputStream output = null;
		try {
			output = new FileOutputStream(dest);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		try {
			output.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			rbc.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return dest.getAbsolutePath();
	}
}
