package com.prince.snmp.tool.agent;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.snmp4j.agent.BaseAgent;
import org.snmp4j.agent.CommandProcessor;
import org.snmp4j.agent.DuplicateRegistrationException;
import org.snmp4j.agent.io.ImportModes;
import org.snmp4j.agent.mo.DefaultMOTable;
import org.snmp4j.agent.mo.MOTableRow;
import org.snmp4j.agent.mo.snmp.RowStatus;
import org.snmp4j.agent.mo.snmp.SnmpCommunityMIB;
import org.snmp4j.agent.mo.snmp.SnmpNotificationMIB;
import org.snmp4j.agent.mo.snmp.SnmpTargetMIB;
import org.snmp4j.agent.mo.snmp.StorageType;
import org.snmp4j.agent.mo.snmp.VacmMIB;
import org.snmp4j.agent.security.MutableVACM;
import org.snmp4j.mp.MPv3;
import org.snmp4j.security.SecurityLevel;
import org.snmp4j.security.SecurityModel;
import org.snmp4j.security.USM;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.Variable;

/**
 * 作为一个客户端被网管软件所管理
 * @author wangzijian
 *
 */
public class MyAgent extends BaseAgent {
	private List<DefaultMOTable> moTables = new ArrayList<DefaultMOTable>();
	private String community;
	
	protected MyAgent(File bootCounterFile, File configFile, List<DefaultMOTable> moTables, String community) {
		super(bootCounterFile, configFile, new CommandProcessor(new OctetString(MPv3.createLocalEngineID())));
		this.moTables = moTables;
		this.community = community;
	}
	
	@Override
	protected void registerManagedObjects() {
		try {
			for (DefaultMOTable table : moTables) {
				server.register(table, null);
			}
		} catch (DuplicateRegistrationException e) {
			e.printStackTrace();
		}
	}
    
	public void startUp(){
		try {
			this.init();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.loadConfig(ImportModes.REPLACE_CREATE);
		this.addShutdownHook();
		this.getServer().addContext(new OctetString(community));
		this.finishInit();
		this.run();
		this.sendColdStartNotification();
	}
	
	/**
	 * to set community
	 */
	@Override
	protected void addCommunities(SnmpCommunityMIB communityMIB) {
		Variable[] com2sec = new Variable[] {
				new OctetString(community),              		// community name
				new OctetString("cpublic"),              	// security name
				getAgent().getContextEngineID(),        	// local engine ID
				new OctetString(community),              		// default context name
				new OctetString(),                      	// transport tag
				new Integer32(StorageType.nonVolatile), 	// storage type
				new Integer32(RowStatus.active)         	// row status
		};
		MOTableRow row =
			communityMIB.getSnmpCommunityEntry().createRow(
					new OctetString("public2public").toSubIndex(true), com2sec);
		communityMIB.getSnmpCommunityEntry().addRow(row);

	}

	@Override
	protected void addNotificationTargets(SnmpTargetMIB arg0,
			SnmpNotificationMIB arg1) {
	}

	@Override
	protected void addUsmUser(USM arg0) {
	}

	@Override
	protected void addViews(VacmMIB vacm) {
		vacm.addGroup(SecurityModel.SECURITY_MODEL_SNMPv1,
				new OctetString("cpublic"),
				new OctetString("v1v2group"),
				StorageType.nonVolatile);

		vacm.addGroup(SecurityModel.SECURITY_MODEL_SNMPv2c,
				new OctetString("cpublic"),
				new OctetString("v1v2group"),
				StorageType.nonVolatile);

		vacm.addAccess(new OctetString("v1v2group"), new OctetString(community),
				SecurityModel.SECURITY_MODEL_ANY,
				SecurityLevel.NOAUTH_NOPRIV,
				MutableVACM.VACM_MATCH_EXACT,
				new OctetString("fullReadView"),
				new OctetString("fullWriteView"),
				new OctetString("fullNotifyView"),
				StorageType.nonVolatile);

		vacm.addViewTreeFamily(new OctetString("fullReadView"), new OID("1.3"),
				new OctetString(), VacmMIB.vacmViewIncluded,
				StorageType.nonVolatile);
		vacm.addViewTreeFamily(new OctetString("fullWriteView"), new OID("1.3"),
				new OctetString(), VacmMIB.vacmViewIncluded,
				StorageType.nonVolatile);
		vacm.addViewTreeFamily(new OctetString("fullNotifyView"), new OID("1.3"),
				new OctetString(), VacmMIB.vacmViewIncluded,
				StorageType.nonVolatile);

		vacm.addViewTreeFamily(new OctetString("restrictedReadView"),
				new OID("1.3.6.1.2"),
				new OctetString(), VacmMIB.vacmViewIncluded,
				StorageType.nonVolatile);
		vacm.addViewTreeFamily(new OctetString("restrictedWriteView"),
				new OID("1.3.6.1.2.1"),
				new OctetString(),
				VacmMIB.vacmViewIncluded,
				StorageType.nonVolatile);
		vacm.addViewTreeFamily(new OctetString("restrictedNotifyView"),
				new OID("1.3.6.1.2"),
				new OctetString(), VacmMIB.vacmViewIncluded,
				StorageType.nonVolatile);
		vacm.addViewTreeFamily(new OctetString("restrictedNotifyView"),
				new OID("1.3.6.1.6.3.1"),
				new OctetString(), VacmMIB.vacmViewIncluded,
				StorageType.nonVolatile);
	}

	@Override
	protected void unregisterManagedObjects() {
		
	}

}
