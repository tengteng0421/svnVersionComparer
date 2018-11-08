package cn.sy.frdz.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Module")
public class Module {
	String svnXmlUrl;
	String mainName;
	String gwt;
	List<Framework> frameworks = new ArrayList<Framework>();
	List<App> apps = new ArrayList<App>();
	List<ProjApp> projApps = new ArrayList<ProjApp>();

	public Module() {
		super();
	}

	public Module(String main, String gwt, List<Framework> frameworks, List<App> apps, List<ProjApp> projApps) {
		super();
		this.mainName = main;
		this.gwt = gwt;
		this.frameworks = frameworks;
		this.apps = apps;
		this.projApps = projApps;

	}

	@XmlAttribute(name="main")
	public String getMain() {
		return mainName;
	}

	@XmlAttribute()
	public String getGwt() {
		return gwt;
	}

	@XmlElement(name = "Framework")
	public List<Framework> getFrameworks() {
		return frameworks;
	}

	@XmlElement(name = "App")
	public List<App> getApps() {
		return apps;
	}

	@XmlElement(name = "ProjApp")
	public List<ProjApp> getProjApps() {
		return projApps;
	}

	public void setGwt(String gwt) {
		this.gwt = gwt;
	}

	public void setMain(String mainName) {
		this.mainName = mainName;
	}

	public void setFrameworks(List<Framework> frameworks) {
		this.frameworks = frameworks;
	}

	public void setApps(List<App> apps) {
		this.apps = apps;
	}

	public void setProjApps(List<ProjApp> projApps) {
		this.projApps = projApps;
	}

	public String getSvnXmlUrl() {
		return svnXmlUrl;
	}

	public void setSvnXmlUrl(String svnXmlUrl) {
		this.svnXmlUrl = svnXmlUrl;
	}
	

}
