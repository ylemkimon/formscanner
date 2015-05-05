package com.albertoborsetta.formscanner.commons;

import com.google.common.collect.AbstractIterator;

public class CharSequenceGenerator extends AbstractIterator<String> {
	private int now = -1;
	private String prefix = "";
	private static final char[] vs;
	static {
		vs = new char['Z' - 'A' + 1];
		for (char i = 'A'; i <= 'Z'; i++)
			vs[i - 'A'] = i;
	}

	private String fixPrefix(String prefix) {
		if (prefix.length() == 0)
			return Character.toString(vs[0]);
		int last = prefix.length() - 1;
		char next = (char) (prefix.charAt(last) + 1);
		String sprefix = prefix.substring(0, last);
		return next - vs[0] == vs.length ? fixPrefix(sprefix) + vs[0] : sprefix
				+ next;
	}

	@Override
	protected String computeNext() {
		if (++now == vs.length)
			prefix = fixPrefix(prefix);
		now %= vs.length;
		return new StringBuilder().append(prefix).append(vs[now]).toString();
	}
}
