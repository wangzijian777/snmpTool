package com.prince.snmp.tool.receiver;

import java.util.List;
import java.util.logging.Logger;
import org.snmp4j.CommandResponder;
import org.snmp4j.CommandResponderEvent;
import org.snmp4j.PDU;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.VariableBinding;

public class CommandResponderImpl implements CommandResponder {
	private final Logger log = Logger.getLogger(CommandResponderImpl.class.getName());
	
	@Override
	public void processPdu(CommandResponderEvent event) {
		PDU pdu = event.getPDU();
		if(PDU.TRAP == pdu.getType()){
			operate(pdu);
		}else{
			log.info("pdu method is:" + pdu.getType() + " not a trap");
		}
		
	}

	private void operate(PDU pdu) {
		List<VariableBinding> bindings = pdu.getBindingList(new OID(".1"));
		
		for (VariableBinding binding : bindings) {
			System.out.println(binding.getOid() + "====" + binding.getVariable().toString());
		}
	}
}
