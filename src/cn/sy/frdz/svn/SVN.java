package cn.sy.frdz.svn;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNProperties;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import cn.sy.frdz.xml.Java22Xml;
import cn.sy.frdz.xml.Module;

public class SVN {
	private static String svnroot;
	private static SVNClientManager clientManager;

	private static void setupLibrary() {
		DAVRepositoryFactory.setup();
		SVNRepositoryFactoryImpl.setup();
		FSRepositoryFactory.setup();
	}

	private static SVNRepository repository = null;
	private static SVN svn;

	public static SVNRepository getRepository() {
		return repository;
	}

	public static SVN auth(String svnRoot, String username, String password) {
		setupLibrary();

		// 创建库连接
		try {
			repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(svnRoot));

			// 身份验证
			ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(username, password);

			// 创建身份验证管理器
			repository.setAuthenticationManager(authManager);

			DefaultSVNOptions options = SVNWCUtil.createDefaultOptions(true);
			SVNClientManager clientManager = SVNClientManager.newInstance(options, authManager);
			SVN.svn = new SVN();
			SVN.svn.svnroot = svnRoot;
			SVN.svn.clientManager = clientManager;
		} catch (SVNException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return SVN.svn;

	}

	@SuppressWarnings("rawtypes")
	public static void main(String[] args) throws SVNException {
		String path = "svn://192.168.199.197/svnroot/";

		auth(path, "tengt", "wdmms:GBBESWDW1");
		Reader reader = getSVNFile("trunk/projects/itn/Main.build.xml", "");
		Module module = (Module) Java22Xml.xmlToBean(reader, Module.class);
		List<String> ss = getSVNDirEntryNames("/tag/applications/App.Authority.Role");
		for (int i = 0; i < ss.size(); i++) {
			System.out.println("---" + ss.get(i));
		}
		// Collection entries =
		// repository.getDir("/tag/applications/App.Authority.Role", -1, null,
		// (Collection) null);
		// Iterator iterator = entries.iterator();
		// while (iterator.hasNext()) {
		// SVNDirEntry entry = (SVNDirEntry) iterator.next();
		// System.out.println(entry.getURL().getPath() + "-----" +
		// entry.getName() + " ( author: '" + entry.getAuthor()
		// + "'; revision: " + entry.getRevision() + "; date: " +
		// entry.getDate());
		// }
		// SVNURL url = SVNURL.parseURIEncoded(svnroot + "/trunk/BaseLibs");

	}

	public static Reader getSVNFile(String url, String fileUrl) {
		SVNProperties properties = new SVNProperties();
		OutputStream outputStream;
		Reader reader = null;
		try {

			String[] xmlU = url.replaceAll("\\\\", "/").split("/");
			String fuleU = fileUrl + "/" + (xmlU[xmlU.length - 1]);
			outputStream = new FileOutputStream(fuleU, false);
			SVNNodeKind node = repository.checkPath(url, 0);
			if (node == null) {
				System.out.println(url + "文件不存在");
			} else if (node != SVNNodeKind.NONE) {
				System.out.println(url + "不是文件");
			}
			url = url.replace("/svnroot", "");
			repository.getFile(url, 0, properties, outputStream);
			outputStream.close();
			reader = new FileReader(new File(fuleU));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return reader;
	}

	public static List<String> getSVNFilesName(String url) {
		List<String> resultList = new ArrayList<String>();
		Collection entries;
		try {
			entries = repository.getDir(url, -1, null, (Collection) null);
			Iterator iterator = entries.iterator();
			while (iterator.hasNext()) {
				SVNDirEntry entry = (SVNDirEntry) iterator.next();
				String name = entry.getName();
				if (name.contains(".build.xml"))
					resultList.add(entry.getURL().getPath());
			}
		} catch (SVNException e) {
			e.printStackTrace();
		}
		return resultList;
	}

	// 获取目录下最高的版本号：只适合tar版本包
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<String> getSVNDirEntryNames(String url) {
		List<String> resultList = new ArrayList<String>();
		Collection entries;
		try {
			entries = repository.getDir(url, -1, null, (Collection) null);
			Iterator iterator = entries.iterator();
			while (iterator.hasNext()) {
				SVNDirEntry entry = (SVNDirEntry) iterator.next();
				resultList.add(entry.getName());
			}
		} catch (SVNException e) {
			e.printStackTrace();
		}
		Collections.sort(resultList, new Comparator() {
			@Override
			public int compare(Object o1, Object o2) {
				long v1 = getSplits(o1);
				long v2 = getSplits(o2);
				return v1 > v2 ? -1 : 1;
			}

			public long getSplits(Object obj) {
				String str = (String) obj;
				String[] s1 = str.split("_");
				str = s1[0];
				String[] s2 = str.split("\\.");
				long resultInt = Long.parseLong(s2[2]);
				resultInt += (Long.parseLong(s2[1]) * 10000L);
				resultInt += (Long.parseLong(s2[0]) * 1000000L);
				return resultInt;
			}
		});
		return resultList;
	}

	/**
	 * 获得所有项目的最后更新时间
	 * 
	 * @author Tengt
	 * @param url
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Map<String, Date> getFilesInfo(String url) {
		Map<String, Date> map = new HashMap<>();
		try {
			Collection dir = repository.getDir(url, -1, null, (Collection) null);
			Iterator iterator = dir.iterator();
			while (iterator.hasNext()) {
				SVNDirEntry entry = (SVNDirEntry) iterator.next();
				Date date = entry.getDate();
				String name = entry.getName();
				// System.out.println(name + " " + date);
				map.put(name, date);

			}
		} catch (SVNException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}
}
