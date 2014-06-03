package com.prince.snmp.tool.agent.data;

import java.util.ArrayList;
import java.util.List;

public class MyMibNode {
	private String name;
	private String oid;
	private String access;
	private String index;
	private String syntax;
	private String descr;
	private String rule;
	private String type;
	private int loop;
	
	private MyMibNode entry;
	
	private List<MyMibNode> columns = new ArrayList<MyMibNode>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public String getAccess() {
		return access;
	}

	public void setAccess(String access) {
		this.access = access;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getSyntax() {
		return syntax;
	}

	public void setSyntax(String syntax) {
		this.syntax = syntax;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public String getRule() {
		return rule;
	}

	public void setRule(String rule) {
		this.rule = rule;
	}

	public MyMibNode getEntry() {
		return entry;
	}

	public void setEntry(MyMibNode entry) {
		this.entry = entry;
	}

	public List<MyMibNode> getColumns() {
		return columns;
	}

	public void setColumns(List<MyMibNode> columns) {
		this.columns = columns;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getLoop() {
		return loop;
	}

	public void setLoop(int loop) {
		this.loop = loop;
	}
}
