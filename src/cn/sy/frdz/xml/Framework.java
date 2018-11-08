package cn.sy.frdz.xml;

import javax.xml.bind.annotation.XmlAttribute;

public class Framework implements SvnBean{
	private String name;
	private String version;
	private boolean includeSource;

	@XmlAttribute()
	public String getName() {
		return name;
	}
	@XmlAttribute()
	public String getVersion() {
		return version;
	}
	@XmlAttribute()
	public boolean isIncludeSource() {
		return includeSource;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public void setIncludeSource(boolean includeSource) {
		this.includeSource = includeSource;
	}
}
