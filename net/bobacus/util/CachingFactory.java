package net.bobacus.util;

import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CachingFactory<T> {

	public CachingFactory(Class<T> klass, Class<?>[] parameterTypes) {
		try {
			Constructor<T> cons = klass.getConstructor(parameterTypes);
			init(cons);
		} catch (Exception e) {
			throw new CachingFactoryException(e);
		}
	}
	
	public CachingFactory(Constructor<T> cons) {
		init(cons);
	}
	
	private void init(Constructor<T> cons) {
		this.cons = cons;
		this.cache = new HashMap<List<?>,T>();
	}
	
	private Constructor<T> cons;
	
	public T getInstance() {
		return getInstance(Collections.EMPTY_LIST);
	}
	
	public synchronized T getInstance(List<?> params) {
		T o = cache.get(params);
		if (o==null) {
			try {
				o = cons.newInstance(params.toArray());
			} catch (Exception e) {
				throw new CachingFactoryException(e);
			}
			System.err.println("CachingFactory - " + cons + " - putting item - " + o + " - for params - " + params);
			cache.put(params, o);
		}
		return o;
	}
	
	private Map<List<?>,T> cache;
}
