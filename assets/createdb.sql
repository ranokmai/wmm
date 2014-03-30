CREATE TABLE ious (
	iou_id INTEGER PRIMARY KEY AUTOINCREMENT,
	item_name TEXT NOT NULL,
	contact TEXT NOT NULL,
	is_contact INTEGER NOT NULL,
	item_type TEXT,
	outbound INTEGER,
	date_borrowed INTEGER,
	date_due INTEGER,
	value REAL,
	picture_loc TEXT,
	notes TEXT);
	