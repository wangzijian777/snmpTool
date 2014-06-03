package com.prince.snmp.tool.util;

public interface Const {
	String SNMP_REQUEST_TYPE_GET = "get";
	String SNMP_REQUEST_TYPE_SET = "set";
	String SNMP_REQUEST_TYPE_TRAP = "trap";
	String SNMP_REQUEST_TYPE_AGENT = "agent";
	String SNMP_REQUEST_TYPE_RECIEVER = "receiver";
	
	String THREAD_POOL_NAME = "snmpServer";
	int AGENT_THREAD_NUM = 5;
	String SNMP_CONFIG_FILE = "snmp.conf";
	String SNMP_AGENT_BC = "SNMP4JAGENTBC.cfg";
	String SNMP_AGENT_CFG = "SNMP4JAGENTCFG.cfg";
	String SNMP_AGENT_MOTALBE_DEFAULT = "datasource.xml";
	String SNMP_TRAP_DATA = "trapdata.xml";
}
