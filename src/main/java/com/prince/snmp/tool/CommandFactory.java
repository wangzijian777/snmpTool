package com.prince.snmp.tool;

import java.util.logging.Logger;
import com.prince.snmp.tool.agent.SnmpSimulator;
import com.prince.snmp.tool.receiver.SnmpReceiver;
import com.prince.snmp.tool.trap.TrapSender;
import com.prince.snmp.tool.trap.dataGenerator.TrapXmlGeneratorImpl;
import com.prince.snmp.tool.util.Const;

/**
 * 根据用户输入的参数构建要执行的命令
 * @author wangzijian
 *
 */
public class CommandFactory {
	private static final Logger log = Logger.getLogger(CommandFactory.class.getName());
	
	public static Command getCommand(String[] args){
		Command cm = null;
		
		if(args.length < 1){
			log.info("plese input arguments");
	 		System.exit(0);
		}
		
		if(Const.SNMP_REQUEST_TYPE_AGENT.equals(args[0])){
			log.info("start as an agent");
			String moTableData = Const.SNMP_AGENT_MOTALBE_DEFAULT;
			if(args.length == 2){
				moTableData = args[1];
			}
			log.info("data config file is " + moTableData);
			cm = new SnmpSimulator(moTableData);
		}
		
		if(Const.SNMP_REQUEST_TYPE_GET.equals(args[0])){
					
		}
		
		if(Const.SNMP_REQUEST_TYPE_RECIEVER.equals(args[0])){
			cm = new SnmpReceiver();
		}
		
		if(Const.SNMP_REQUEST_TYPE_SET.equals(args[0])){
			//TODO wait to implement
		}
		
		if(Const.SNMP_REQUEST_TYPE_TRAP.equals(args[0])){
			log.info("send a trap message");
			String trapData = Const.SNMP_TRAP_DATA;
			if(args.length == 2){
				trapData = args[1];
			}
			log.info("data config file is " + trapData);
			cm = new TrapSender(new TrapXmlGeneratorImpl(trapData));
		}
		
	 	if(cm == null){
	 		log.info("argument is error, only get,agent,set,trap,walk,receiver can input");
	 		System.exit(0);
	 	}
	 	
	 	return cm;
	}
}
