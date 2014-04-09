CREATE TABLE ious (
	iou_id INTEGER PRIMARY KEY AUTOINCREMENT,
	item_name TEXT NOT NULL,
	contact TEXT NOT NULL,
	is_contact INTEGER NOT NULL,
	item_type TEXT,
	outbound INTEGER,
	date_borrowed TEXT,
	date_due TEXT,
	date_completed TEXT,
	value TEXT,
	picture_loc TEXT,
	notes TEXT);
	
CREATE TABLE archived_ious (
	iou_id INTEGER PRIMARY KEY AUTOINCREMENT,
	item_name TEXT NOT NULL,
	contact TEXT NOT NULL,
	is_contact INTEGER NOT NULL,
	item_type TEXT,
	outbound INTEGER,
	date_borrowed TEXT,
	date_due TEXT,
	date_completed TEXT,
	value TEXT,
	picture_loc TEXT,
	notes TEXT);