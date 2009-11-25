package de.mpg.mpiz.koeln.anna;

import java.util.Properties;

import org.eclipse.core.runtime.adaptor.EclipseStarter;

import de.kerner.commons.ee.beans.ManagedBean;
import de.kerner.commons.logging.Log;

public class ExecutorBean implements ManagedBean {

	private static final long serialVersionUID = 910513119916511786L;
	private static final Log log = new Log(ExecutorBean.class);

	public String getIdentString() {
		return "#{executorBean}";
	}
	
	public String execute(){
		
		try {
			log.debug("map");
			Properties properties = new Properties();
	        EclipseStarter.setInitialProperties(properties);
			EclipseStarter.startup(new String[] { "-clean" }, null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "success";
	}

}
