package com.asena.scimgateway.script;

import org.junit.jupiter.api.Test;

public class StringEvaluatorTest {

	String[][] data = { { "test", "A" }, { "test1", "B" }, { "test2", "1" } };

	@Test
	void scriptRunnerTest() {
		String testStr = "Hello$function.getSystem($function.lelo($function.remo(${test}oo${test1}$sdddsstest)s$function.remo2(AooB$sdddsstest)))${test2}";

	}

}