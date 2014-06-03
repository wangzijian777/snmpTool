package com.prince.snmp.tool.util;

import java.util.ArrayList;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import com.prince.snmp.tool.agent.data.MyMibNode;

public class XmlParse {
	public static List<MyMibNode> parseXmlToMibTables(Document doc){
		List<MyMibNode> nodes = new ArrayList<MyMibNode>();
		
		for (Object o : doc.getRootElement().element("tables").elements("table")) {
			MyMibNode tableNode = new MyMibNode();
			Element table = (Element)o;
			tableNode.setName(table.attributeValue("name"));
			tableNode.setOid(table.attributeValue("oid"));
			tableNode.setLoop(Integer.parseInt(table.attributeValue("loop")));
			
			Element entry = table.element("entry");
			MyMibNode entryNode = new MyMibNode();
			entryNode.setName(entry.attributeValue("name"));
			entryNode.setOid(entry.attributeValue("oid"));
			tableNode.setEntry(entryNode);
			
			List<MyMibNode> columns = new ArrayList<MyMibNode>();
			for (Object co: table.element("body").elements("column")) {
				Element column = (Element)co;
				MyMibNode columnNode = new MyMibNode();
				columnNode.setName(column.attributeValue("name"));
				columnNode.setOid(column.attributeValue("oid"));
				columnNode.setIndex(column.attributeValue("index"));
				columnNode.setAccess(column.attributeValue("access"));
				columnNode.setType(column.attributeValue("type"));
				columnNode.setSyntax(column.elementText("syntax"));
				columnNode.setDescr(column.elementText("descr"));
				columnNode.setRule(column.elementText("rule"));
				columns.add(columnNode);
			}
			
			tableNode.setColumns(columns);
			nodes.add(tableNode);
		}
		return nodes;
	}
	
	public static Document parseMibTablesToXml(List<MyMibNode> nodes){
		Document doc = DocumentHelper.createDocument();
		Element root = doc.addElement("datasource");
		Element tables = root.addElement("tables");
		for (MyMibNode node : nodes) {
			Element table = tables.addElement("table");
			table.addAttribute("name", node.getName());
			table.addAttribute("oid", node.getOid());
			table.addAttribute("loop", "1");
			
			MyMibNode entryNode = node.getEntry();
			Element entry = table.addElement("entry");
			entry.addAttribute("name", entryNode.getName());
			entry.addAttribute("oid", entryNode.getOid());
			
			Element body = table.addElement("body");
			List<MyMibNode> columns =  node.getColumns();
			for(MyMibNode columnNode : columns){
				Element column = body.addElement("column");
				column.addAttribute("name", columnNode.getName());
				column.addAttribute("oid", columnNode.getOid());
				column.addAttribute("index", columnNode.getIndex());
				column.addAttribute("access", columnNode.getAccess());
				column.addAttribute("type", columnNode.getType());
				column.addElement("syntax").addText(columnNode.getSyntax());
				column.addElement("descr").addText(columnNode.getDescr());
				column.addElement("rule").addText(columnNode.getRule());
			}
		}
		
		return doc;
	}
}
