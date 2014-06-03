package com.prince.snmp.tool;

import java.io.IOException;

/**
 * run this tool as the following format:
 * java -jar SnmpTool.jar simulator fileName
 * @author wangzijian
 *
 */
public class StartUp {
	public static void main(String[] args) {
		try {
			CommandFactory.getCommand(args).startUp();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
