package com.prince.snmp.tool.agent;

import com.prince.snmp.tool.Command;
import com.prince.snmp.tool.agent.data.MOTableGenerator;
import com.prince.snmp.tool.util.Configure;
import com.prince.snmp.tool.util.Const;

/**
 * Æô¶¯Ò»¸öAgent
 * @author wangzijian
 *
 */
public class SnmpSimulator implements Command{
	private String moTableData;
	
	public SnmpSimulator(String moTableData) {
		this.moTableData = moTableData;
	}
	@Override
	public void startUp() {
		new MyAgent(Configure.getInstance().getResourceFile(Const.SNMP_AGENT_BC), 
				Configure.getInstance().getResourceFile(Const.SNMP_AGENT_CFG), 
				MOTableGenerator.generateMoTableFromXml(Configure.getInstance().getResourceFile(moTableData)), 
				Configure.getInstance().getCommunity()).startUp();
		while (true) {
			try {
				Thread.sleep(1000);
			}
			catch (InterruptedException ex1) {
				break;
			}
		}
	}
	
	public static void main(String[] args) {
		new SnmpSimulator("datasource.xml").startUp();
	}

}
