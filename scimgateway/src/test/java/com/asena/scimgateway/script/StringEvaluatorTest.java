package com.asena.scimgateway.script;

import org.junit.jupiter.api.Test;

public class StringEvaluatorTest {
	String variablePrefix = "${";
	String variableSuffix = "}";
	String functionPrefix = "$function.";
	String functionStart = "(";
	String functionEnd = ")";
	String[][] data = { { "test", "A" }, { "test1", "B" }, { "test2", "1" } };

	@Test
	void scriptRunnerTest() {
		String testStr = "Hello$function.getSystem($function.lelo($function.remo(${test}oo${test1}$sdddsstest)s$function.remo2(AooB$sdddsstest)))${test2}";

		String expandedStr = expandStr(testStr);
		System.out.println(expandedStr);
		String l = evalStr(expandedStr);
		System.out.println(l);
	}

	String evalStr(String str) {
		String retStr = str;
		int idxFunctionStart = str.indexOf(functionPrefix);
		int idxFunctionEnd = -1;
		if (idxFunctionStart >= 0) {
			idxFunctionEnd = str.indexOf(functionEnd, idxFunctionStart);
			if (idxFunctionEnd > idxFunctionStart) {
				idxFunctionEnd++;
				int idxLastFunctionStart = str.lastIndexOf(functionPrefix, idxFunctionEnd);
				String strPrefix = str.substring(0, idxLastFunctionStart);
				String strFunctionCall = str.substring(idxLastFunctionStart + functionPrefix.length(), idxFunctionEnd);
				String strSuffix = str.substring(idxFunctionEnd);
				int idxFirstParenthese = strFunctionCall.indexOf(functionStart);

				if (idxFirstParenthese >= 0) {
					String strFunctionName = strFunctionCall.substring(0, idxFirstParenthese);
					String strParameter = strFunctionCall.substring(idxFirstParenthese + functionStart.length());
					strParameter = strParameter.substring(0, strParameter.length() - 1);
					String strCallResult = "kek";
					retStr = strPrefix + strCallResult + strSuffix;
				} else {
					retStr = strPrefix + strSuffix;
				}
				idxFunctionStart = retStr.indexOf(functionPrefix);
				if (idxFunctionStart >= 0) {
					return evalStr(retStr);
				}
			}
		}
		return retStr;
	}

	String expandStr(String str) {
		int idxAttr = str.indexOf(variablePrefix);
		int idxAttrFinish = -1;
		String retStr = str;
		if (idxAttr >= 0) {
			idxAttrFinish = str.indexOf("}", idxAttr + variablePrefix.length());
			if (idxAttrFinish > idxAttr) {
				String prefix = str.substring(0, idxAttr);
				String strAttr = str.substring(idxAttr + variablePrefix.length(), idxAttrFinish);
				String suffix = str.substring(idxAttrFinish + variableSuffix.length());
				retStr = expandStr(prefix + getValue(strAttr) + suffix);
			}
		}
		return retStr;
	}

	String getValue(String attrName) {
		String retStr = "";
		for (String[] s : data) {
			if (s[0].equals(attrName)) {
				return s[1];
			}
		}
		return retStr;
	}
}