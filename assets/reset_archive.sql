DROP TABLE archived_ious;	

CREATE TABLE archived_ious (
	iou_id INTEGER PRIMARY KEY AUTOINCREMENT,
	item_name TEXT NOT NULL,
	contact TEXT NOT NULL,
	is_contact INTEGER NOT NULL,
	item_type INTEGER,
	outbound INTEGER,
	date_borrowed TEXT,
	date_due TEXT,
	date_completed TEXT,
	value REAL,
	picture_loc TEXT,
	notes TEXT);