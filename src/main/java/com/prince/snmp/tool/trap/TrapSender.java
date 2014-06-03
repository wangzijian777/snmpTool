package com.prince.snmp.tool.trap;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Vector;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import com.prince.snmp.tool.Command;
import com.prince.snmp.tool.trap.dataGenerator.ITrapGenerator;
import com.prince.snmp.tool.util.Configure;

public class TrapSender implements Command{
	private Snmp snmp;
	private Address targetAddress;
	private ITrapGenerator generator;
	
	public TrapSender(ITrapGenerator generator) {
		this.generator = generator;
		targetAddress = GenericAddress.parse(Configure.getInstance().getUdpTrapIpPort());
		TransportMapping<UdpAddress> transport = null;
		try {
			transport = new DefaultUdpTransportMapping();
			snmp = new Snmp(transport);
			transport.listen();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendTrap(){
		CommunityTarget target = new CommunityTarget();
		target.setAddress(targetAddress);
		target.setRetries(Configure.getInstance().getRetries());
		target.setTimeout(Configure.getInstance().getTimeOut());
		target.setCommunity(new OctetString(Configure.getInstance().getCommunity()));
		target.setVersion(Configure.getInstance().getSnmpVersion());
		
		List<TrapData> datas = generator.generateTrapData();
		
		try {
			for (TrapData trapData : datas) {
				sendPdu(trapData, target);
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void sendPdu(TrapData trapData, CommunityTarget target) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, IOException {
		PDU pdu = MoToPdu.moToPdu(trapData);
		ResponseEvent respEvnt = snmp.send(pdu, target);
		
		// Ω‚ŒˆResponse  
        if (respEvnt != null && respEvnt.getResponse() != null) {  
            Vector<VariableBinding> recVBs = (Vector<VariableBinding>) respEvnt.getResponse().getVariableBindings();  
            for (int i = 0; i < recVBs.size(); i++) {  
                VariableBinding recVB = recVBs.elementAt(i);  
                System.out.println(recVB.getOid() + " : " + recVB.getVariable());  
            }  
        }
	}

	@Override
	public void startUp() throws IOException {
		sendTrap();
	}
	
}
