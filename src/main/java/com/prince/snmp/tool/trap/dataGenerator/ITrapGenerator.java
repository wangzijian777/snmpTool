package com.prince.snmp.tool.trap.dataGenerator;

import java.util.List;
import com.prince.snmp.tool.trap.TrapData;

public interface ITrapGenerator {
	public List<TrapData> generateTrapData();
}
