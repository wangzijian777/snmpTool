package com.prince.snmp.tool.trap;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import org.apache.commons.beanutils.PropertyUtils;
import org.snmp4j.PDU;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.TimeTicks;
import org.snmp4j.smi.Variable;
import org.snmp4j.smi.VariableBinding;

import com.prince.snmp.tool.o2p.Oid;

public class MoToPdu {

	public static PDU moToPdu(TrapData trap) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		PDU pdu = new PDU();
		pdu.setType(PDU.TRAP);
		
		pdu.add(new VariableBinding(SnmpConstants.sysUpTime, new TimeTicks(trap.getSysUpTime())));
		pdu.add(new VariableBinding(SnmpConstants.snmpTrapOID, new OID(trap.getTrapOid())));
		
		addVarBindings(trap, pdu);
		
		return pdu;
	}
	
	/**
	 * iterator the fields, trans to varBinding and add to pdu
	 * @param varBingdings
	 * @param pdu
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	private static void addVarBindings(TrapData trap, PDU pdu) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		if(trap.getMo() != null){
			for (Field f : trap.getMo().getClass().getDeclaredFields()){
				String oid = f.getAnnotation(Oid.class).oid();
				pdu.add(new VariableBinding(new OID(oid), javaToSnmp(PropertyUtils.getProperty(trap.getMo(), f.getName()))));
			}
		}else{
			for (VariableBinding vb : trap.getVariableBindings()) {
				pdu.add(vb);
			}
		}
	}
	
	private static Variable javaToSnmp(Object property) {
		Variable v = null;
		if(String.class == property.getClass()){
			v = new OctetString(property.toString());
		}
		
		if(Integer.class == property.getClass()){
			v = new Integer32(Integer.parseInt(property.toString()));
		}
		return v;
	}
}
