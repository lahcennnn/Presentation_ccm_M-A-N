package com.cedicam.gm.bt.bean;

import java.lang.reflect.Method;

public class ConfirmBean {

	private String title;
	private String body;
	private String yesLabel;
	private String noLabel;
	private Object yesBean;
	private Object noBean;
	private String yesActionMethod;
	private String noActionMethod;



	public String confirm(){
		Method method;
		try {
			method = yesBean.getClass().getMethod(yesActionMethod, (Class<?>[])null);
			String result = (String) method.invoke(yesBean, (Object)null);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}

	}

	public String cancel(){
		Method method;
		try {
			method = noBean.getClass().getMethod(noActionMethod, (Class<?>[]) null);
			String result = (String) method.invoke(noBean, (Object)null);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getYesLabel() {
		return yesLabel;
	}

	public void setYesLabel(String yesLabel) {
		this.yesLabel = yesLabel;
	}

	public String getNoLabel() {
		return noLabel;
	}

	public void setNoLabel(String noLabel) {
		this.noLabel = noLabel;
	}

	public Object getYesBean() {
		return yesBean;
	}

	public void setYesBean(Object yesBean) {
		this.yesBean = yesBean;
	}

	public Object getNoBean() {
		return noBean;
	}

	public void setNoBean(Object noBean) {
		this.noBean = noBean;
	}

	public String getYesActionMethod() {
		return yesActionMethod;
	}

	public void setYesActionMethod(String yesActionMethod) {
		this.yesActionMethod = yesActionMethod;
	}

	public String getNoActionMethod() {
		return noActionMethod;
	}

	public void setNoActionMethod(String noActionMethod) {
		this.noActionMethod = noActionMethod;
	}
}
