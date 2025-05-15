
-- DEPARTMENTS
INSERT INTO `department` (id, name, created_at, updated_at, is_deleted) VALUES
(1, 'Computer Science', NOW(), NOW(), FALSE),
(2, 'Mechanical Engineering', NOW(), NOW(), FALSE);

-- COURSES
INSERT INTO `course` (code, name, department_id, created_at, updated_at, is_deleted) VALUES
(101, 'Software Engineering', 1, NOW(), NOW(), FALSE),
(102, 'Data Structures', 1, NOW(), NOW(), FALSE),
(103, 'Operating Systems', 1, NOW(), NOW(), FALSE),
(104, 'Database Systems', 1, NOW(), NOW(), FALSE),
(201, 'Thermodynamics', 2, NOW(), NOW(), FALSE),
(202, 'Fluid Mechanics', 2, NOW(), NOW(), FALSE),
(203, 'Heat Transfer', 2, NOW(), NOW(), FALSE),
(204, 'Machine Design', 2, NOW(), NOW(), FALSE);

-- USERS
INSERT INTO `user` (id, name, email, password, phone_number, address, created_at, updated_at, is_deleted, role, is_super, department_id) VALUES
(1, 'Nyazul Ansari', 'ansarinyazul2003@gmail.com', 'nyaz@inny', '8551055201', 'Ozar, Nashik', NOW(), NOW(), FALSE, 'ADMIN', TRUE, NULL),
(2, 'Alan Admin', 'alan.admin@example.com', 'admin123', '9999999992', 'Admin Lane 2', NOW(), NOW(), FALSE, 'ADMIN', FALSE, NULL),
(3, 'Amy Admin', 'amy.admin@example.com', 'admin123', '9999999993', 'Admin Lane 3', NOW(), NOW(), FALSE, 'ADMIN', FALSE, NULL),
(4, 'Ben Basic', 'ben.basic@example.com', 'basic123', '8888888881', 'Basic Street 1', NOW(), NOW(), FALSE, 'BASIC_EMPLOYEE', FALSE, NULL),
(5, 'Beth Basic', 'beth.basic@example.com', 'basic123', '8888888882', 'Basic Street 2', NOW(), NOW(), FALSE, 'BASIC_EMPLOYEE', FALSE, NULL),
(6, 'Bill Basic', 'bill.basic@example.com', 'basic123', '8888888883', 'Basic Street 3', NOW(), NOW(), FALSE, 'BASIC_EMPLOYEE', FALSE, NULL),
(7, 'Eve Followup', 'eve.emp@example.com', 'follow123', '7777777771', 'Emp Road 1', NOW(), NOW(), FALSE, 'FOLLOWUP_EMPLOYEE', FALSE, 1),
(8, 'Ed Followup', 'ed.emp@example.com', 'follow123', '7777777772', 'Emp Road 2', NOW(), NOW(), FALSE, 'FOLLOWUP_EMPLOYEE', FALSE, 2),
(9, 'Ella Followup', 'ella.emp@example.com', 'follow123', '7777777773', 'Emp Road 3', NOW(), NOW(), FALSE, 'FOLLOWUP_EMPLOYEE', FALSE, 1);

-- TASKS
INSERT INTO `task` (id, title, body, due_date, user_id, created_at, updated_at, is_completed) VALUES
(1, 'Prepare report', 'Monthly metrics', DATE_ADD(NOW(), INTERVAL 1 DAY), 4, NOW(), NOW(), FALSE),
(2, 'Update website', 'New course info', DATE_ADD(NOW(), INTERVAL 2 DAY), 5, NOW(), NOW(), FALSE),
(3, 'Fix bug', 'Resolve login issue', DATE_ADD(NOW(), INTERVAL 3 DAY), 6, NOW(), NOW(), TRUE),
(4, 'Call student', 'Discuss application', DATE_ADD(NOW(), INTERVAL 1 DAY), 7, NOW(), NOW(), FALSE),
(5, 'Schedule meeting', 'With department head', DATE_ADD(NOW(), INTERVAL 4 DAY), 8, NOW(), NOW(), TRUE),
(6, 'Prepare slides', 'Lead presentation', DATE_ADD(NOW(), INTERVAL 2 DAY), 9, NOW(), NOW(), FALSE);

-- LEADS
INSERT INTO `lead` (id, name, email, phone_number, address, created_at, updated_at, is_deleted) VALUES
(1, 'John Doe', 'john@lead.com', '7894561201', 'Lead Street 1', NOW(), NOW(), FALSE),
(2, 'Jane Smith', 'jane@lead.com', '7894561202', 'Lead Street 2', NOW(), NOW(), FALSE),
(3, 'Mike Ross', 'mike@lead.com', '7894561203', 'Lead Street 3', NOW(), NOW(), FALSE),
(4, 'Rachel Zane', 'rachel@lead.com', '7894561204', 'Lead Street 4', NOW(), NOW(), FALSE),
(5, 'Harvey Specter', 'harvey@lead.com', '7894561205', 'Lead Street 5', NOW(), NOW(), FALSE);

-- FOLLOWUPS
INSERT INTO `followup` (id, lead_id, description, status, due_date, created_at, updated_at, is_deleted, course_id, employee_id) VALUES
(1, 1, 'Discuss course structure', 'PENDING', DATE_ADD(NOW(), INTERVAL 3 DAY), NOW(), NOW(), FALSE, 101, 7),
(2, 2, 'Follow-up on fees', 'IN_PROGRESS', DATE_ADD(NOW(), INTERVAL 4 DAY), NOW(), NOW(), FALSE, 102, 7),
(3, 3, 'Application help', 'COMPLETED', DATE_ADD(NOW(), INTERVAL 5 DAY), NOW(), NOW(), FALSE, 201, 8),
(4, 4, 'Scholarship discussion', 'OVERDUE', DATE_ADD(NOW(), INTERVAL -2 DAY), NOW(), NOW(), FALSE, 202, 9),
(5, 5, 'Final confirmation', 'CANCELLED', DATE_ADD(NOW(), INTERVAL 1 DAY), NOW(), NOW(), FALSE, 203, 8);

-- FOLLOWUP_NODEs
INSERT INTO `followup_node` (id, title, body, date, done_by, created_at, is_deleted, followup_id) VALUES
(1, 'Intro Call', 'Spoke to lead about goals.', NOW(), 7, NOW(), FALSE, 1),
(2, 'Details Shared', 'Sent course brochure.', NOW(), 7, NOW(), FALSE, 2),
(3, 'Form Assistance', 'Helped fill application.', NOW(), 8, NOW(), FALSE, 3),
(4, 'Docs Pending', 'Lead didn’t send docs.', NOW(), 9, NOW(), FALSE, 4),
(5, 'Call Canceled', 'Lead dropped off.', NOW(), 8, NOW(), FALSE, 5);
