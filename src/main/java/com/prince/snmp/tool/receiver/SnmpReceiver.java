package com.prince.snmp.tool.receiver;

import java.io.IOException;
import java.util.logging.Logger;
import org.snmp4j.MessageDispatcherImpl;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.mp.MPv2c;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.MultiThreadedMessageDispatcher;
import org.snmp4j.util.ThreadPool;
import com.prince.snmp.tool.Command;
import com.prince.snmp.tool.util.Configure;
import com.prince.snmp.tool.util.Const;

/**
 * 构建一个多线程的Trap Receiver
 * @author wangzijian
 *
 */
public class SnmpReceiver implements Command{
	private final Logger log = Logger.getLogger(SnmpReceiver.class.getName());

	@Override
	public void startUp() throws IOException {
		log.info("Snmp Trap Receiver Start");
		log.info("listened on " + Configure.getInstance().getUdpTrapIpPort());
		ThreadPool pool = ThreadPool.create(Const.THREAD_POOL_NAME, Const.AGENT_THREAD_NUM);
		MultiThreadedMessageDispatcher dispatcher = new MultiThreadedMessageDispatcher(pool, new MessageDispatcherImpl());
		Address listenAddress = GenericAddress.parse(Configure.getInstance().getUdpTrapIpPort());
		TransportMapping transport = new DefaultUdpTransportMapping((UdpAddress) listenAddress);
		// 构建SNMP，并且使其开始监听
		Snmp snmp = new Snmp(dispatcher, transport);
        snmp.getMessageDispatcher().addMessageProcessingModel(new MPv2c());
        snmp.listen();
        snmp.addCommandResponder(new CommandResponderImpl());
	}
}
