package de.mpg.mpiz.koeln.anna.core.starter;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;

public class Activator implements BundleActivator {
	
	private class BundleStarter implements Callable<Void> {
		
		private final Collection<Bundle> installedBundles;
		
		BundleStarter(Collection<Bundle> installedBundles) {
			this.installedBundles = installedBundles;
		}

		public Void call() throws Exception {
			startBundles(installedBundles);
			return null;
		}
		
		private void startBundles(Collection<Bundle> installedBundles) throws BundleException {
			if (installedBundles.size() == 0){
				System.err.println("no plugins started");
				return;}
			for (Bundle b : installedBundles) {
				b.start();
			}
		}
	}

	private class BundleInstaller implements Callable<Collection<Bundle>> {

		private final File path;
		private final BundleContext context;

		BundleInstaller(final BundleContext context, final File path) {
			this.path = path;
			this.context = context;
		}

		public Collection<Bundle> call() throws Exception {
			final Collection<String> bundlePathes = getBundlePathes(path);
			final Collection<Bundle> installedBundles = installBundles(bundlePathes);
			return installedBundles;
		}

		private Collection<Bundle> installBundles(
				Collection<String> bundlePathes) throws BundleException {
			if (bundlePathes.size() == 0){
				System.err.println("no plugins installed");
				return Collections.emptyList();}
			final List<Bundle> result = new ArrayList<Bundle>();
			for (String p : bundlePathes) {
				System.err.println("installing " + p);
				final Bundle b = context.installBundle(p);
				result.add(b);
			}
			return result;
		}

		private Collection<String> getBundlePathes(File path) throws NoPluginsFoundException {
			final List<String> result = new ArrayList<String>();
			final File[] content = path.listFiles();
			if (content == null || content.length == 0) {
				System.err.println("content of dir ="+content);
//				throw new NoPluginsFoundException("Could not find any plugins in " + path);
				return Collections.emptyList();
			} else {
				for (File f : content) {
					if (f.isFile()) {
						if (f.canRead()) {
							final String s = f.toURI().toString();
							if (s.endsWith(".jar")) {
								System.err.println("Adding " + f
										+ " to known plugins list.");
								result.add(s);
							}
						} else {
							System.err.println("Cannot read " + f
									+ ". Skipping.");
						}
					} else {
						// ignore dirs
					}
				}
			}
			return result;
		}
	}

	private final static String PLUGINS_PATH_0 = System.getProperty("user.dir")
			+ "/00-framework/";
	private final static String PLUGINS_PATH_1 = System.getProperty("user.dir")
	+ "/01-libs/";
	private final static String PLUGINS_PATH_2 = System.getProperty("user.dir")
	+ "/02-anna-libs/";
	private final static String PLUGINS_PATH_3 = System.getProperty("user.dir")
	+ "/03-anna-core/";
	private final static String PLUGINS_PATH_4 = System.getProperty("user.dir")
	+ "/04-anna-dataserver/";
	private final static String PLUGINS_PATH_5 = System.getProperty("user.dir")
	+ "/05-anna-server/";
	private final static String PLUGINS_PATH_6 = System.getProperty("user.dir")
	+ "/06-anna-steps/";
	private final ExecutorService exe = Executors.newSingleThreadExecutor();
	public void start(BundleContext context) throws Exception {
		synchronized (this) {
			final Collection<Bundle> c = exe.submit(new BundleInstaller(context, new File(PLUGINS_PATH_0))).get();
			c.addAll(exe.submit(new BundleInstaller(context, new File(PLUGINS_PATH_1))).get());
			c.addAll(exe.submit(new BundleInstaller(context, new File(PLUGINS_PATH_2))).get());
			c.addAll(exe.submit(new BundleInstaller(context, new File(PLUGINS_PATH_3))).get());
			c.addAll(exe.submit(new BundleInstaller(context, new File(PLUGINS_PATH_4))).get());
			c.addAll(exe.submit(new BundleInstaller(context, new File(PLUGINS_PATH_5))).get());
			c.addAll(exe.submit(new BundleInstaller(context, new File(PLUGINS_PATH_6))).get());
			exe.submit(new BundleStarter(c)).get();
		}
	}

	public void stop(BundleContext context) throws Exception {
		System.out.println(this + " stopping");
	}

}
