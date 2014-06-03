package com.prince.snmp.tool.agent.data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.dom4j.Document;
import org.snmp4j.agent.MOAccess;
import org.snmp4j.agent.mo.DefaultMOMutableRow2PC;
import org.snmp4j.agent.mo.DefaultMOTable;
import org.snmp4j.agent.mo.MOAccessImpl;
import org.snmp4j.agent.mo.MOColumn;
import org.snmp4j.agent.mo.MOMutableColumn;
import org.snmp4j.agent.mo.MOMutableTableModel;
import org.snmp4j.agent.mo.MOTableIndex;
import org.snmp4j.agent.mo.MOTableSubIndex;
import org.snmp4j.smi.Counter32;
import org.snmp4j.smi.Gauge32;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.IpAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.SMIConstants;
import org.snmp4j.smi.TimeTicks;
import org.snmp4j.smi.Variable;
import com.prince.snmp.tool.util.DomTool;
import com.prince.snmp.tool.util.TestDataUtil;
import com.prince.snmp.tool.util.XmlParse;

public class MOTableGenerator {
	public static List<DefaultMOTable> generateMoTableFromXml(File xmlPath){
		List<DefaultMOTable> tables = new ArrayList<DefaultMOTable>();
		Document doc = DomTool.parseDocFromXml(xmlPath);
		List<MyMibNode> nodes = XmlParse.parseXmlToMibTables(doc);
		
		for (MyMibNode node : nodes) {
			tables.add(parseToMOTable(node));
		}
		return tables;
	}
	
	/**
	 * generate data by table node
	 * @param node
	 * @return
	 */
	private static DefaultMOTable parseToMOTable(MyMibNode node) {
		// set Index
		MOTableSubIndex[] subIndexes =
				new MOTableSubIndex[] { new MOTableSubIndex(SMIConstants.SYNTAX_INTEGER) };
		MOTableIndex indexDef = new MOTableIndex(subIndexes, false);
		
		// set columns
		List<MyMibNode> columnNodes = node.getColumns();
		MOColumn<Variable>[] columns = new MOColumn[columnNodes.size()];
		for (int i = 0; i < columnNodes.size(); i++) {
			MyMibNode columnNode = columnNodes.get(i);
			columns[i] = parseNodeToColumn(columnNode, i);
		}
		
		// set table
		DefaultMOTable ifTable = new DefaultMOTable(new OID(node.getEntry().getOid()), indexDef, columns);
		
		// set datas
		MOMutableTableModel model = (MOMutableTableModel) ifTable.getModel();
		for (int i = 0; i < node.getLoop(); i++) {
			Variable[] rowValues = generateRowData(columnNodes);
			model.addRow(new DefaultMOMutableRow2PC(new OID(String.valueOf(i + 1)), rowValues));
		}
		ifTable.setVolatile(true);
		return ifTable;
	}

	private static Variable[] generateRowData(List<MyMibNode> columnNodes) {
		Variable[] rowValues = new Variable[columnNodes.size()];
		for (int i = 0; i < columnNodes.size(); i++) {
			MyMibNode columnNode = columnNodes.get(i);
			String rule = columnNode.getRule();
			
			if("Counter32".equals(columnNode.getType())){
				rowValues[i] = new Counter32(Long.parseLong("".equals(rule) ? TestDataUtil.parseInnerMethod("${randomInt(1, 512)}") : TestDataUtil.parseInnerMethod(rule)));
			}
			
			if("Gauge32".equals(columnNode.getType())){
				rowValues[i] = new Gauge32(Long.parseLong("".equals(rule) ? TestDataUtil.parseInnerMethod("${randomInt(1, 512)}") : TestDataUtil.parseInnerMethod(rule)));
			}
			
			if("Integer32".equals(columnNode.getType()) || "INTEGER".equals(columnNode.getType())){
				rowValues[i] = new Integer32(Integer.parseInt("".equals(rule) ? TestDataUtil.parseInnerMethod("${randomInt(1, 512)}") : TestDataUtil.parseInnerMethod(rule)));
			}
			
			if("TimeTicks".equals(columnNode.getType())){
				rowValues[i] = new TimeTicks();
			}
			
			if("Unsigned32".equals(columnNode.getType())){
				rowValues[i] = new Integer32(Integer.parseInt("".equals(rule) ? TestDataUtil.parseInnerMethod("${randomInt(1, 512)}") : TestDataUtil.parseInnerMethod(rule)));
			}
			
			if("IpAddress".equals(columnNode.getType())){
				rowValues[i] = new IpAddress("10.10.10.10");
			}
			
			if("OCTET STRING".equals(columnNode.getType())){
				rowValues[i] = new OctetString("".equals(rule) ? TestDataUtil.parseInnerMethod("${randomString(5)}") : TestDataUtil.parseInnerMethod(rule));
			}
		}
		return rowValues;
	}

	private static MOColumn<Variable> parseNodeToColumn(MyMibNode columnNode, int i) {
		if(Boolean.parseBoolean(columnNode.getIndex())){
			return new MOColumn<Variable>(i + 1, parseTypeToSyntax(columnNode.getType()), new MOAccessImpl(0));
		}
		return new MOColumn<Variable>(i + 1, parseTypeToSyntax(columnNode.getType()), parseAccess(columnNode.getAccess()));
	}

	private static MOAccess parseAccess(String access) {
		if("read-create".equals(access)){
			return MOAccessImpl.ACCESS_READ_CREATE;
		}
		
		if("read-only".equals(access)){
			return MOAccessImpl.ACCESS_READ_ONLY;
		}
		
		if("read-write".equals(access)){
			return MOAccessImpl.ACCESS_READ_WRITE;
		}
		
		if("read-only".equals(access)){
			return MOAccessImpl.ACCESS_WRITE_ONLY;
		}
		
		return MOAccessImpl.ACCESS_FOR_NOTIFY;
	}

	private static int parseTypeToSyntax(String type) {
		if("Counter32".equals(type)){
			return SMIConstants.SYNTAX_COUNTER32;
		}
		
		if("Gauge32".equals(type)){
			return SMIConstants.SYNTAX_GAUGE32;
		}
		
		if("Integer32".equals(type) || "INTEGER".equals(type)){
			return SMIConstants.SYNTAX_INTEGER;
		}
		
		if("TimeTicks".equals(type)){
			return SMIConstants.SYNTAX_TIMETICKS;
		}
		
		if("Unsigned32".equals(type)){
			return SMIConstants.SYNTAX_INTEGER32;
		}
		
		if("IpAddress".equals(type)){
			return SMIConstants.SYNTAX_IPADDRESS;
		}
		
		if("OCTET STRING".equals(type)){
			return SMIConstants.SYNTAX_OCTET_STRING;
		}
		
		return 0;
	}

	public static List<DefaultMOTable> generateMoTableTest(){
		List<DefaultMOTable> tables = new ArrayList<DefaultMOTable>();
		tables.add(createMpcRdStatsTable());
		return tables;
	}
	
	/**
	 * mpcRdStatsTable
	 **/
	private static DefaultMOTable createMpcRdStatsTable() {
		MOTableSubIndex[] subIndexes =
			new MOTableSubIndex[] { new MOTableSubIndex(SMIConstants.SYNTAX_INTEGER) };
		MOTableIndex indexDef = new MOTableIndex(subIndexes, false);
		MOColumn[] columns = new MOColumn[4];
		int c = 0;
		columns[c++] =
			new MOColumn(c, SMIConstants.SYNTAX_INTEGER,
					new MOAccessImpl(0));     // mpcRdStatGroupID -> not-accessible
		
		columns[c++] =
			new MOMutableColumn(c, SMIConstants.SYNTAX_INTEGER,     // mpcRdStatReset
					MOAccessImpl.ACCESS_READ_WRITE, null);
		
		columns[c++] =
			new MOColumn(c, SMIConstants.SYNTAX_COUNTER32,
					MOAccessImpl.ACCESS_READ_ONLY);// mpcRdStatNumAutoSwitchovers	
		
		columns[c++] =
			new MOColumn(c, SMIConstants.SYNTAX_COUNTER32,
					MOAccessImpl.ACCESS_READ_ONLY);     // mpcRdStatNumManualSwitchovers
		

		DefaultMOTable ifTable =
			new DefaultMOTable(new OID("1.3.6.1.4.1.7569.1.2.1.62.1"), indexDef, columns);
		MOMutableTableModel model = (MOMutableTableModel) ifTable.getModel();


		int count = 0;
		int[] list = {1,2,3,4,5,6};
		for (int element : list) {
			String elemVal = "";
			
			Variable[] rowValues1 = new Variable[] {
					new Integer32(324),
					new Integer32(765),
					new Counter32(33),
					new Counter32(49)
			};
			model.addRow(new DefaultMOMutableRow2PC(new OID(elemVal.valueOf(element)), rowValues1));	
			count++;
		}

		ifTable.setVolatile(true);
		return ifTable;
	}
}
