package cn.sy.frdz.main;

import java.io.File;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tmatesoft.svn.core.SVNException;

import cn.sy.frdz.svn.SVN;
import cn.sy.frdz.xml.Java22Xml;
import cn.sy.frdz.xml.Module;
import cn.sy.frdz.xml.SvnBean;

public class BeginMainInternet {
	@SuppressWarnings("unused")
	public static void main(String[] args) throws SVNException {
		String path = args[0];// "svn://192.168.199.197/svnroot/";
		String svnName = args[1];// "suny";
		if (svnName == null || svnName.isEmpty()) {
			svnName = "suny";
		}
		String svnPwd = args[2];// "Frdz19940227@";
		if (svnPwd == null || svnPwd.isEmpty()) {
			svnPwd = "Frdz19940227@";
		}
		SVN.auth(path, svnName, svnPwd);
		String fileUrl = args[3];// "D:/testClear/";
		String projectUrl = args[4];// "trunk/projects/itn/"
		List<Module> moduleS = getBuildXmlClass(projectUrl, fileUrl + "/oldfiles");
		// beanComparer(moduleS);
		createNewXml(fileUrl + "/newfiles", moduleS);

	}

	private static void createNewXml(String fileUrl, List<Module> moduleS) {
		File file = new File(fileUrl);
		if (!file.exists()) {
			file.mkdirs();
		}
		for (Module module : moduleS) {
			String xmlUrl = module.getSvnXmlUrl();
			String[] xmlU = xmlUrl.replaceAll("\\\\", "/").split("/");
			Java22Xml.beanToXml(module, fileUrl + "/" + xmlU[xmlU.length - 1]);
		}

	}

	private static void beanComparer(List<Module> moduleS) {
		for (Module m : moduleS) {
			updataModule("Framework", new ArrayList<SvnBean>(m.getFrameworks()));
			updataModule("App", new ArrayList<SvnBean>(m.getApps()));
		}
	}

	private static void updataModule(String type, List<SvnBean> svnBeanS) {
		for (SvnBean svnBean : svnBeanS) {
			String name = svnBean.getName();
			String value = versionCache.get(name);
			if (value != null && !value.isEmpty()) {
				svnBean.setVersion(value);
				return;
			}
			String url = "/tag";
			if ("App".equals(type)) {
				url += "/applications";
			} else if ("Framework".equals(type)) {
				url += "/frameworks";

			}
			url += "/" + name;
			String version = SVN.getSVNDirEntryNames(url).get(0);
			svnBean.setVersion(version);
			versionCache.put(name, version);
		}
	}

	private static Map<String, String> versionCache = new HashMap<>();

	private static List<Module> getBuildXmlClass(String url, String fileUrl) {
		File file = new File(fileUrl);
		if (!file.exists()) {
			file.mkdirs();
		}
		List<Module> moduleS = new ArrayList<>();
		List<String> xmlFileS = SVN.getSVNFilesName(url);
		for (String xmlUrl : xmlFileS) {
			Reader reader = SVN.getSVNFile(xmlUrl, fileUrl);
			Module module = (Module) Java22Xml.xmlToBean(reader, Module.class);
			module.setSvnXmlUrl(xmlUrl);
			moduleS.add(module);
		}

		return moduleS;
	}

}
