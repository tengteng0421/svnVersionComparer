package cn.sy.frdz.xml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class Java22Xml {

	public static void main(String[] args) {
		// xml转换为java对象
		Module m = (Module) xmlToBean("D:/testClear/newfiles/test.xml", Module.class);
		beanToXml(m,"D:/testClear/newfiles/TTTTTT2.xml");
		System.out.println("success");
		// "D:\testClear"
		// java对象转换为xml格式
		/*
		 * Group group = new Group; Student s = new Student; s.setId(123123);
		 * s.setName("测试中文"); Student s1 = new Student; s1.setId(123123);
		 * s1.setName("测试中文"); group.setGroupId(111);
		 * group.setGroupName("fredzhanghao"); List<Student> list = new
		 * ArrayList<Student>; list.add(s); list.add(s1);
		 * group.setStudents(list); new XmlUtil.beanToXml(group);
		 */
	}

	public static Module getModule(String url) {
		return (Module) xmlToBean(url, Module.class);
	}

	@SuppressWarnings("unchecked")
	public static <T> T xmlToBean(String urlStr, Class classType) {
		T t = null;
		try {
			URL url = Java22Xml.class.getClassLoader().getResource(urlStr);
			FileReader fr;
			if (url == null) {
				fr = new FileReader(urlStr);
			} else {
				fr = new FileReader(url.getPath());
			}
			t = xmlToBean(fr, classType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return t;
	}

	public static <T> T xmlToBean(Reader reader, Class classType) {
		T t = null;
		try {
			JAXBContext context = JAXBContext.newInstance(classType);
			Unmarshaller unMarshaller = context.createUnmarshaller();
			t = (T) unMarshaller.unmarshal(reader);
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return t;
	}

	/**
	 * java对象转换为xml
	 * 
	 * @param <T>
	 */
	public static <T> String beanToXml(T t, String url) {
		StringBuffer buffer = new StringBuffer();
		JAXBContext context;
		Marshaller marshaller;
		try {
			// 利用jdk中自带的转换类实现
			context = JAXBContext.newInstance(t.getClass());
			marshaller = context.createMarshaller();
			// 将对象转换成输出流形式的xml
			marshaller.marshal(t, new File(url));

			// InputStreamReader reader = new InputStreamReader(new
			// FileInputStream(url));
			// int temp;
			// while ((temp = reader.read()) != -1) {
			// buffer.append((char) temp);
			// }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buffer.toString();
	}

	/**
	 * Java类向XML进行转换
	 * 
	 * @param cls
	 * @param obj
	 * @throws JAXBException
	 */

	public static <T> String java2Xml(Class<T> cls, Object obj) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(cls);
		Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		StringWriter writer = new StringWriter();
		marshaller.marshal(obj, writer);
		return writer.toString();
	}

}
