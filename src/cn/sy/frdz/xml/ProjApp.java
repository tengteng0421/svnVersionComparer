package cn.sy.frdz.xml;

import javax.xml.bind.annotation.XmlAttribute;

public class ProjApp {
	private String name;

	@XmlAttribute()
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
