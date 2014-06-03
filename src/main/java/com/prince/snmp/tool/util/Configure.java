package com.prince.snmp.tool.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import org.snmp4j.mp.SnmpConstants;

/**
 * 存储SNMP的配置
 * @author wangzijian
 *
 */
public class Configure {
	private static Configure configure;
	
	/**
	 * 从配置文件读取SNMP的配置信息
	 * 主要包括：IP，端口，Community
	 * @return
	 */
	public static Configure getInstance(){
		if(configure == null){
			configure = new Configure();
		}
		return configure;
	}
	
	private Configure(){
		// read configure from configure file
		Properties prop = getProperties("." + File.separator + Const.SNMP_CONFIG_FILE);
		this.udpSnmpIpPort = prop.getProperty("UDP_SNMP_IP_PORT");
		this.udpTrapIpPort = prop.getProperty("UDP_TRAP_IP_PORT");
		this.trapPort = Integer.parseInt(prop.getProperty("TRAP_PORT"));
		this.community = prop.getProperty("SNMP_COMMUNITY");
		this.snmpVersion = transSnmpVersion(prop.getProperty("SNMP_VERSION"));
		this.retries = Integer.parseInt(prop.getProperty("SNMP_RETRIES"));
		this.timeOut = Long.parseLong(prop.getProperty("SNMP_TIME_OUT"));
	}
	
	/**
	 * 1-v1,2-v2,3-v2c
	 * @param property
	 * @return
	 */
	private int transSnmpVersion(String property) {
		if("1".equals(property)){
			return SnmpConstants.version1;
		}
		return SnmpConstants.version2c;
	}

	private Properties getProperties(String fileName) {
		Properties prop = new Properties();
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(getResourceFile(fileName));
			prop.load(fis);
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return prop;
	}
	
	/**
	 * 得到属性文件,在项目中指向bin的根目录，被打成jar包之后，指向jar包所在目录
	 * 跟当前JVM配置的ProtectionDomain有关
	 * @param fileName
	 * @return
	 */
	public File getResourceFile(String fileName) {
		File resource = null;
		String path = Configure.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		File parent = new File(path);
		if(parent.isDirectory()){
			resource = new File(parent, fileName);
		}else{
			resource = new File(parent.getParent(), fileName);
		}
		
		return resource;
	}

	private String udpSnmpIpPort;
	private String udpTrapIpPort;
	private int trapPort;
	private String community;
	private int snmpVersion;
	private int retries;
	private long timeOut;

	public String getUdpTrapIpPort() {
		return udpTrapIpPort;
	}

	public void setUdpTrapIpPort(String udpTrapIpPort) {
		this.udpTrapIpPort = udpTrapIpPort;
	}

	public int getTrapPort() {
		return trapPort;
	}

	public void setTrapPort(int trapPort) {
		this.trapPort = trapPort;
	}

	public String getUdpSnmpIpPort() {
		return udpSnmpIpPort;
	}

	public void setUdpSnmpIpPort(String udpSnmpIpPort) {
		this.udpSnmpIpPort = udpSnmpIpPort;
	}

	public String getCommunity() {
		return community;
	}

	public void setCommunity(String community) {
		this.community = community;
	}

	public int getSnmpVersion() {
		return snmpVersion;
	}

	public void setSnmpVersion(int snmpVersion) {
		this.snmpVersion = snmpVersion;
	}

	public int getRetries() {
		return retries;
	}

	public void setRetries(int retries) {
		this.retries = retries;
	}

	public long getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(long timeOut) {
		this.timeOut = timeOut;
	}
}
