package com.efithealth.app.javabean;

public class MassageOrderTime {

	private String time;
	private boolean isSelected=false;
	private int flage_ischecked=0;
	
	public int getFlage_ischecked() {
		return flage_ischecked;
	}
	public void setFlage_ischecked(int flage_ischecked) {
		this.flage_ischecked = flage_ischecked;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public boolean isSelected() {
		return isSelected;
	}
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	
}
