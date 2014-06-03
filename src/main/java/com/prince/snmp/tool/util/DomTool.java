package com.prince.snmp.tool.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class DomTool {
	public static void writeDoc(Document doc, String xmlPath){
		 OutputFormat format = OutputFormat.createPrettyPrint(); 
		XMLWriter xmlWriter = null;
		try {
			xmlWriter = new XMLWriter(new FileOutputStream(new File(xmlPath)),format);
			xmlWriter.write(doc);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				xmlWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static Document parseDocFromXml(File xmlPath) {
		SAXReader saxReader = new SAXReader();
		Document doc = null;
		try {
			doc = saxReader.read(xmlPath);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return doc;
	}
}
