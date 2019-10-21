INSERT INTO image (id, name, file_name, employee) VALUES
(1, 'photo1', null, 'employee'),
(2, 'photo2', null, 'manager'),
(3, 'photo3', null, 'assistantManager');

INSERT INTO declaration (id, costs, approved_local, approved_global, employee, instance_id, image_id) VALUES
(1, 12.0, FALSE, FALSE, 'employee', 'instanceid', 1),
(2, 12.0, FALSE, FALSE, 'employee', 'instanceid', 1),
(3, 12.0, FALSE, FALSE, 'manager', 'instanceid', 2),
(4, 12.0, FALSE, FALSE, 'manager', 'instanceid', 2);