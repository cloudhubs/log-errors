package ires.baylor.edu.logerrors.parser;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import ires.baylor.edu.logerrors.parser.TracebackParser;

class TracebackParserTest {

	@ParameterizedTest(name = "{index} => trace={0}, oracle={1}")
	@MethodSource("addTracebackProvider")
	void testAddTraceback(String trace, List<String> oracle) {
		TracebackParser tbp = new TracebackParser();
		List<String> result = tbp.addTraceback(new Scanner(trace));
		assertEquals(oracle, result);
	}

	@SuppressWarnings("unused")
	private static Stream<Arguments> addTracebackProvider() {
		return Stream.of(Arguments.of(
				"File \"/opt/app-root/lib/python3.6/site-packages/insights/core/dr.py\", line 661, in invoke\r\n" + 
				"return self.component(*args)\r\n" + 
				"File \"/opt/app-root/lib/python3.6/site-packages/ccx_ocp_core/models/nodes.py\", line 108, in Nodes\r\n" + 
				"int(node.q.status.capacity.memory.value.split(\"Ki\")[0]) / (1000 * 1000), 2\r\n" + 
				"AttributeError: 'NoneType' object has no attribute 'split'",
				List.of("File \"/opt/app-root/lib/python3.6/site-packages/insights/core/dr.py\", line 661, in invoke\n" + 
						"return self.component(*args)",
						"File \"/opt/app-root/lib/python3.6/site-packages/ccx_ocp_core/models/nodes.py\", line 108, in Nodes\n" + 
						"int(node.q.status.capacity.memory.value.split(\"Ki\")[0]) / (1000 * 1000), 2")),
				Arguments.of("not a proper entry", List.of()));
	}
}
