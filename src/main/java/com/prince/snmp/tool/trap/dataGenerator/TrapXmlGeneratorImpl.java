package com.prince.snmp.tool.trap.dataGenerator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.Element;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.TimeTicks;
import org.snmp4j.smi.Variable;
import org.snmp4j.smi.VariableBinding;
import com.prince.snmp.tool.trap.TrapData;
import com.prince.snmp.tool.util.DomTool;
import com.prince.snmp.tool.util.TestDataUtil;

public class TrapXmlGeneratorImpl implements ITrapGenerator {
	private String fileName;
	public TrapXmlGeneratorImpl(String fileName) {
		this.fileName = fileName;
	}
	
	@Override
	public List<TrapData> generateTrapData() {
		List<TrapData> trapDatas = new ArrayList<TrapData>();
		Document doc = DomTool.parseDocFromXml(TestDataUtil.getResourceFile("." + File.separator + "trapdata" + File.separator + fileName));
		Element root = doc.getRootElement();
		List<Element> trapDataEs = root.elements("trapData");
		int times = Integer.parseInt(root.attributeValue("times"));
		for (int i = 0; i < times; i++) {
			for (Element e : trapDataEs) {
				trapDatas.add(generateTrapData(e));
			}
		}
		
		return trapDatas;
	}

	private TrapData generateTrapData(Element e) {
		TrapData data = new TrapData();
		String trapOid = TestDataUtil.parseInnerMethod(e.element("trapOid").getText());
		data.setTrapOid(trapOid);
		data.setSysUpTime(Long.parseLong(e.element("sysUpTime").getText()));
		data.setVariableBindings(getVarBindings(e.element("varBindings")));
		return data;
	}

	private List<VariableBinding> getVarBindings(Element varBindingEs) {
		List<VariableBinding> vbs = new ArrayList<VariableBinding>();
		List<Element> es = varBindingEs.elements("varBinding");
		
		for (Element e : es) {
			String oid = e.attributeValue("oid");
			String type = e.attributeValue("type");
			String value = e.getText();
			vbs.add(new VariableBinding(new OID(oid), getValueByType(TestDataUtil.parseInnerMethod(value), type)));
		}
		
		return vbs;
	}

	private Variable getValueByType(String value, String type) {
		Variable v = null;
		if("OctetString".equals(type)){
			v = new OctetString(value);
		}
		if("Integer32".equals(type)){
			v = new Integer32(Integer.parseInt(value));
		}
		if("OID".equals(type)){
			v = new OID(value);
		}
		if("TimeTicks".equals(type)){
			v = new TimeTicks(Long.parseLong(value));
		}
		return v;
	}

}
