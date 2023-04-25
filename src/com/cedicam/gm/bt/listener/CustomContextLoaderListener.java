/**
 * 
 */
package com.cedicam.gm.bt.listener;

import org.springframework.web.context.ContextLoaderListener;

/**
 * @author CDENIS
 *
 */
public class CustomContextLoaderListener extends ContextLoaderListener {

	public CustomContextLoaderListener() {
		super();
		System.getProperties().put("org.apache.el.parser.COERCE_TO_ZERO", "false");
	}
	
}
