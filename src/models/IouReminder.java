package models;

import java.util.Date;

public class IouReminder {
	private Long reminder_id;
	public Long iou_id;
	public Iou iou;
	public Date reminder_time;
	
	/**
	 * @return the reminder_id
	 */
	public Long getReminder_id() {
		return reminder_id;
	}
	/**
	 * @param reminder_id the reminder_id to set
	 */
	public void setReminder_id(Long reminder_id) {
		this.reminder_id = reminder_id;
	}
}
