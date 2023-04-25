package com.cedicam.gm.bt.util;

import java.util.LinkedList;
import java.util.List;

public final class ToList<T> {

	public List<T> ArrayToList(T[] tab) {
		if (tab == null) {
			return null;
		} else {
			List<T> list = new LinkedList<T>();
			for (T t : tab) {
				list.add(t);
			}
			return list;
		}
	}

}
