package com.prince.snmp.tool.trap;

import java.io.Serializable;
import java.util.List;
import org.snmp4j.smi.VariableBinding;

import com.prince.snmp.tool.o2p.Mo;

/**
 * a whole info to send content parameters and vars
 * @author wangzijian
 *
 */
public class TrapData implements Serializable{
	private static final long serialVersionUID = -4429079515545243404L;
	
	// parameters
	private String trapOid;
	private long sysUpTime;
	
	private Mo mo;
	private List<VariableBinding> variableBindings;

	public String getTrapOid() {
		return trapOid;
	}

	public void setTrapOid(String trapOid) {
		this.trapOid = trapOid;
	}

	public long getSysUpTime() {
		return sysUpTime;
	}

	public void setSysUpTime(long sysUpTime) {
		this.sysUpTime = sysUpTime;
	}

	public Mo getMo() {
		return mo;
	}

	public void setMo(Mo mo) {
		this.mo = mo;
	}

	public List<VariableBinding> getVariableBindings() {
		return variableBindings;
	}

	public void setVariableBindings(List<VariableBinding> variableBindings) {
		this.variableBindings = variableBindings;
	}
}
