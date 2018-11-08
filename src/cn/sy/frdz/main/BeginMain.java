package cn.sy.frdz.main;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.sy.frdz.excel.OutputExcel;
import cn.sy.frdz.xml.Java22Xml;
import cn.sy.frdz.xml.Module;
import cn.sy.frdz.xml.SvnBean;

public class BeginMain {

	public static void main(String[] args) {
		String fileUrl = args[3];
		locationMain(fileUrl);
	}

	public static String locationMain(String url) {
		List<Module> nowList = getModules(url + "/newfiles");
		List<Module> oldList = getModules(url + "/oldfiles");
		Map<String, List<Map<String, String>>> dataMap = beanComparer(nowList, oldList);
		for (String key : dataMap.keySet()) {
			OutputExcel.OutputExcel(url + "/提测.xls", "版本号变更", dataMap.get(key));
		}
		System.out.println("success-------");
		return "Success";
	}

	private static Map<String, List<Map<String, String>>> beanComparer(List<Module> nowList, List<Module> oldList) {
		Map<String, List<Map<String, String>>> resultMap = new HashMap<>();
		for (Module nowBean : nowList) {
			String nowMain = nowBean.getMain();
			for (Module oldBean : oldList) {
				String oldMain = oldBean.getMain();
				if (nowMain.equals(oldMain)) {
					List<Map<String, String>> beanList = new ArrayList<>();
					Map<String, String> map = new HashMap<>();
					map.put("name", nowMain);
					map.put("oldVersion", "");
					map.put("newVersion", "");
					beanList.add(map);
					beanList.addAll(elementComparer(new ArrayList<SvnBean>(nowBean.getFrameworks()),
							new ArrayList<SvnBean>(oldBean.getFrameworks())));
					beanList.addAll(elementComparer(new ArrayList<SvnBean>(nowBean.getApps()),
							new ArrayList<SvnBean>(oldBean.getApps())));

					resultMap.put(nowMain, beanList);
					continue;
				}
			}
		}
		return resultMap;
	}

	private static List<Map<String, String>> elementComparer(List<SvnBean> nowFrameworks, List<SvnBean> oldFrameworks) {
		List<Map<String, String>> dataList = new ArrayList<>();
		for (SvnBean nframework : nowFrameworks) {
			String nName = nframework.getName();
			for (SvnBean oframework : oldFrameworks) {
				String oName = oframework.getName();
				if (nName.equals(oName)) {
					Map<String, String> map = new HashMap<>();
					map.put("name", nName);
					map.put("oldVersion", oframework.getVersion());
					map.put("newVersion", nframework.getVersion());
					dataList.add(map);
					continue;
				}
			}
		}
		return dataList;

	}

	private static List<Module> getModules(String url) {
		List<String> files = getModuleClass(url);
		List<Module> listModule = new ArrayList<>();
		for (String f : files) {
			Module bean = Java22Xml.getModule(f);
			listModule.add(bean);
		}
		return listModule;

	}

	public static List<String> getModuleClass(String url) {
		List<String> reslutList = new ArrayList<>();
		File fileDirectory = new File(url);
		if (fileDirectory.isDirectory()) {
			File[] files = fileDirectory.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isFile()) {// 判断是否为文件
					File ff = files[i];
					String fileUrl = files[i].getAbsolutePath();
					reslutList.add(fileUrl);
				}
			}
		}

		return reslutList;
		// TODO
	}
}
