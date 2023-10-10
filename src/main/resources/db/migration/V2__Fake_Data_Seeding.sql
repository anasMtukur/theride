SET SCHEMA 'theride';
INSERT INTO end_user (created_at, updated_at, id, username, password, fullname, email, mobile, block_code, is_deleted, is_blocked, reset_token) VALUES ('2018-03-22 06:33:27.829', '2018-03-22 06:33:27.829', '4e6787fc-446f-11e8-842f-0ed5f89f718b', 'user@demo.com', '$2a$12$elZYY9rgEBRhk/HnPlK6qOhkq6aDkEg4lIkADm0nuqHMXBbGXd742', 'Dgreat User', 'user@demo.com', '+2348166617281', NULL, false, false, NULL);
INSERT INTO end_user (created_at, updated_at, id, username, password, fullname, email, mobile, block_code, is_deleted, is_blocked, reset_token) VALUES ('2023-10-06 14:35:07.959', '2023-10-06 14:35:07.959', '393135c7-65eb-4185-92dc-49d11592af72', 'anasmtukur91@gmail.com', '$2a$10$kFm9kDCKKdUIeXcXhoqmj.Nx/jJmhnm9so2/MPdATzHq7n0Ov7/XG', 'Anas Tukur', 'anasmtukur91@gmail.com', '+2348020967114', NULL, false, false, NULL);
INSERT INTO end_user (created_at, updated_at, id, username, password, fullname, email, mobile, block_code, is_deleted, is_blocked, reset_token) VALUES ('2023-10-08 14:47:12.595', '2023-10-08 14:47:12.595', 'ff1c7866-3c52-488a-b4a8-37335aedf4d8', 'maxroady@gmail.com', '$2a$10$3k23e0uEkVy.laCWqFtP1e9Vpf298V0lKQVqHsbypaPaC0LNWPW4e', 'Maxis Roady', 'maxroady@gmail.com', '0182550876', NULL, false, false, NULL);
INSERT INTO end_user (created_at, updated_at, id, username, password, fullname, email, mobile, block_code, is_deleted, is_blocked, reset_token) VALUES ('2023-10-08 14:49:03.57', '2023-10-08 14:49:03.57', 'db1dcb1d-ef43-4b8e-a735-54ac3faeb8a7', 'axel@foley.com', '$2a$10$08AMN.lvdNp8ObVWdxfhpOXUBAVogNSClGg0agjHw/yDlpDJFhtLi', 'Axel Foley', 'axel@foley.com', '08066671387', NULL, false, false, NULL);
INSERT INTO end_user (created_at, updated_at, id, username, password, fullname, email, mobile, block_code, is_deleted, is_blocked, reset_token) VALUES ('2023-10-08 14:55:22.939', '2023-10-08 14:55:22.939', '239cc7ef-c4a4-40b4-b9ec-f25fa79a8d66', 'normy@test.com', '$2a$10$.3vdXFOAAAdEbj2jy5KqCOkUmqUn6FD.vORVuSDzE430NAXSJacwC', 'Norm Mcdonald', 'normy@test.com', '08066691387', NULL, false, false, NULL);
INSERT INTO end_user (created_at, updated_at, id, username, password, fullname, email, mobile, block_code, is_deleted, is_blocked, reset_token) VALUES ('2018-03-22 06:33:27.829', '2018-03-22 06:33:27.829', '42a511d2-446f-11e8-842f-0ed5f89f718b', 'admin@theride.com', '$2a$12$elZYY9rgEBRhk/HnPlK6qOhkq6aDkEg4lIkADm0nuqHMXBbGXd742', 'System Manager', 'admin@theride.com', '+2348166697281', NULL, false, false, NULL);


--
-- Data for Name: drivers; Type: TABLE DATA; Schema: theride; Owner: theride
--

INSERT INTO drivers (created_at, updated_at, id, end_user, first_name, last_name, gender, license_number, active_city, approval_status) VALUES ('2023-10-08 15:39:08.169', '2023-10-08 20:16:42.63', 'ca27310d-f656-4968-8f22-5dcccefe37f3', '239cc7ef-c4a4-40b4-b9ec-f25fa79a8d66', 'Norm', 'Mcdonald', '1', '9928374738', 'Kano', 'APPROVED');


--
-- Data for Name: passengers; Type: TABLE DATA; Schema: theride; Owner: theride
--

INSERT INTO passengers (created_at, updated_at, id, end_user, first_name, last_name, gender) VALUES ('2023-10-07 16:46:22.198', '2023-10-07 16:46:22.198', '3e908490-e659-4a4e-9161-45212545217f', '393135c7-65eb-4185-92dc-49d11592af72', 'Anas', 'Tukur', '1');
INSERT INTO passengers (created_at, updated_at, id, end_user, first_name, last_name, gender) VALUES ('2023-10-08 20:56:33.384', '2023-10-08 20:56:33.384', 'e397acba-0386-4d29-916e-ef7fa214cec1', '239cc7ef-c4a4-40b4-b9ec-f25fa79a8d66', 'Norm', 'Mcdonald', '1');


--
-- Data for Name: cars; Type: TABLE DATA; Schema: theride; Owner: theride
--

INSERT INTO cars (created_at, updated_at, id, driver, car_type, color, brand_model, plate_no) VALUES ('2023-10-08 15:39:08.194', '2023-10-08 15:39:08.194', '8637f529-0076-4ed0-9634-ea4e92070ebb', 'ca27310d-f656-4968-8f22-5dcccefe37f3', 'SEDAN', 'Red', 'Hyundai Coupe', 'DAM955');


--
-- Data for Name: roles; Type: TABLE DATA; Schema: theride; Owner: theride
--

INSERT INTO roles (created_at, updated_at, id, name) VALUES ('2018-03-22 06:33:27.829', '2018-03-22 06:33:27.829', '42a511d3-447f-11e8-842f-0ed5f89f718b', 'ROLE_USER');
INSERT INTO roles (created_at, updated_at, id, name) VALUES ('2018-03-22 06:33:27.829', '2018-03-22 06:33:27.829', '15f974d1-acb6-4d33-847b-9aca0e0decce', 'ROLE_DRIVER');
INSERT INTO roles (created_at, updated_at, id, name) VALUES ('2018-03-22 06:33:27.829', '2018-03-22 06:33:27.829', '9797c405-10cf-49fe-bfd3-30bc9127b51f', 'ROLE_ADMIN');
INSERT INTO roles (created_at, updated_at, id, name) VALUES ('2018-03-22 06:33:27.829', '2018-03-22 06:33:27.829', '56dd6d18-b3c8-429e-88b2-44cd129ed4c5', 'ROLE_SUPER_ADMIN');


--
-- Data for Name: enduser_role; Type: TABLE DATA; Schema: theride; Owner: theride
--

INSERT INTO enduser_role (created_at, updated_at, id, end_user, role) VALUES ('2018-03-22 06:33:27.833825', '2018-03-22 06:33:27.833825', 'ee24dc86-2d9a-11e8-b657-0239e68bba6a', '42a511d2-446f-11e8-842f-0ed5f89f718b', '9797c405-10cf-49fe-bfd3-30bc9127b51f');
INSERT INTO enduser_role (created_at, updated_at, id, end_user, role) VALUES ('2018-03-22 06:33:27.833825', '2018-03-22 06:33:27.833825', '451b4e62-2d9b-11e8-94d8-0239e68bba6a', '4e6787fc-446f-11e8-842f-0ed5f89f718b', '42a511d3-447f-11e8-842f-0ed5f89f718b');
INSERT INTO enduser_role (created_at, updated_at, id, end_user, role) VALUES ('2023-10-06 14:35:08.067', '2023-10-06 14:35:08.067', '8b7a8d94-82a7-4ad7-99d6-6c9b1fae9c6a', '393135c7-65eb-4185-92dc-49d11592af72', '42a511d3-447f-11e8-842f-0ed5f89f718b');
INSERT INTO enduser_role (created_at, updated_at, id, end_user, role) VALUES ('2023-10-06 14:35:08.068', '2023-10-06 14:35:08.068', '1e6af507-c8bc-4f73-b898-e532398a0118', '393135c7-65eb-4185-92dc-49d11592af72', '42a511d3-447f-11e8-842f-0ed5f89f718b');
INSERT INTO enduser_role (created_at, updated_at, id, end_user, role) VALUES ('2023-10-08 14:47:12.617', '2023-10-08 14:47:12.617', '000c2e40-a10c-46fd-8780-f00decb4de14', 'ff1c7866-3c52-488a-b4a8-37335aedf4d8', '42a511d3-447f-11e8-842f-0ed5f89f718b');
INSERT INTO enduser_role (created_at, updated_at, id, end_user, role) VALUES ('2023-10-08 14:47:12.617', '2023-10-08 14:47:12.617', '25aff43e-ed3b-4115-b017-38df81531275', 'ff1c7866-3c52-488a-b4a8-37335aedf4d8', '42a511d3-447f-11e8-842f-0ed5f89f718b');
INSERT INTO enduser_role (created_at, updated_at, id, end_user, role) VALUES ('2023-10-08 14:49:03.583', '2023-10-08 14:49:03.583', 'e113cfad-1fc5-40e4-920e-7d9021966e6d', 'db1dcb1d-ef43-4b8e-a735-54ac3faeb8a7', '42a511d3-447f-11e8-842f-0ed5f89f718b');
INSERT INTO enduser_role (created_at, updated_at, id, end_user, role) VALUES ('2023-10-08 14:49:03.583', '2023-10-08 14:49:03.583', 'c600175d-c7b8-41d7-9734-e67005417ca9', 'db1dcb1d-ef43-4b8e-a735-54ac3faeb8a7', '42a511d3-447f-11e8-842f-0ed5f89f718b');
INSERT INTO enduser_role (created_at, updated_at, id, end_user, role) VALUES ('2023-10-08 14:55:22.963', '2023-10-08 14:55:22.963', '2aa05603-5174-4b31-9c8d-aa7811bf624d', '239cc7ef-c4a4-40b4-b9ec-f25fa79a8d66', '42a511d3-447f-11e8-842f-0ed5f89f718b');
INSERT INTO enduser_role (created_at, updated_at, id, end_user, role) VALUES ('2023-10-08 14:55:22.963', '2023-10-08 14:55:22.963', '3f06018a-524d-4205-afd4-0ca6149f7c6b', '239cc7ef-c4a4-40b4-b9ec-f25fa79a8d66', '42a511d3-447f-11e8-842f-0ed5f89f718b');
INSERT INTO enduser_role (created_at, updated_at, id, end_user, role) VALUES ('2023-10-08 20:20:52.802', '2023-10-08 20:20:52.802', '58ef6bb2-7cbe-44df-841f-a1bbad14d90b', '239cc7ef-c4a4-40b4-b9ec-f25fa79a8d66', '42a511d3-447f-11e8-842f-0ed5f89f718b');
INSERT INTO enduser_role (created_at, updated_at, id, end_user, role) VALUES ('2023-10-08 20:20:52.807', '2023-10-08 20:20:52.807', '0815832b-2d46-4491-ab69-1c5daea0c9e9', '239cc7ef-c4a4-40b4-b9ec-f25fa79a8d66', '15f974d1-acb6-4d33-847b-9aca0e0decce');

--
-- Data for Name: locations; Type: TABLE DATA; Schema: theride; Owner: theride
--

INSERT INTO locations (created_at, updated_at, id, driver, latitude, longitude, geo_hash_zone) VALUES ('2023-10-07 09:36:01.293512', '2023-10-07 09:36:01.293512', '8bc0ba94-64ec-11ee-ae9d-47a7287c241b', 'ca27310d-f656-4968-8f22-5dcccefe37f3', '37.7912', '-122.4194', '9q8yyr8vtnr2');
INSERT INTO locations (created_at, updated_at, id, driver, latitude, longitude, geo_hash_zone) VALUES ('2023-10-07 09:36:01.293512', '2023-10-07 09:36:01.293512', '8bc0ba95-64ec-11ee-ae9d-47a7287c241b', 'ca27310d-f656-4968-8f22-5dcccefe37f3', '37.7635', '-122.402', '9q8yydts6dts');
INSERT INTO locations (created_at, updated_at, id, driver, latitude, longitude, geo_hash_zone) VALUES ('2023-10-07 09:36:01.293512', '2023-10-07 09:36:01.293512', '8bc0ba96-64ec-11ee-ae9d-47a7287c241b', 'ca27310d-f656-4968-8f22-5dcccefe37f3', '37.7823', '-122.4371', '9q8yvyh1bg1n');
INSERT INTO locations (created_at, updated_at, id, driver, latitude, longitude, geo_hash_zone) VALUES ('2023-10-07 09:36:01.293512', '2023-10-07 09:36:01.293512', '8bc0ba97-64ec-11ee-ae9d-47a7287c241b', 'ca27310d-f656-4968-8f22-5dcccefe37f3', '37.7801', '-122.408', '9q8yyt9jnc5d');
INSERT INTO locations (created_at, updated_at, id, driver, latitude, longitude, geo_hash_zone) VALUES ('2023-10-07 09:36:01.293512', '2023-10-07 09:36:01.293512', '8bc0ba98-64ec-11ee-ae9d-47a7287c241b', 'ca27310d-f656-4968-8f22-5dcccefe37f3', '37.7979', '-122.4194', '9q8zn2butpr2');
INSERT INTO locations (created_at, updated_at, id, driver, latitude, longitude, geo_hash_zone) VALUES ('2023-10-07 09:36:01.293512', '2023-10-07 09:36:01.293512', '8bc0ba99-64ec-11ee-ae9d-47a7287c241b', 'ca27310d-f656-4968-8f22-5dcccefe37f3', '37.769', '-122.4385', '9q8yvgeh25vk');
INSERT INTO locations (created_at, updated_at, id, driver, latitude, longitude, geo_hash_zone) VALUES ('2023-10-07 09:36:01.293512', '2023-10-07 09:36:01.293512', '8bc0ba9a-64ec-11ee-ae9d-47a7287c241b', 'ca27310d-f656-4968-8f22-5dcccefe37f3', '37.7749', '-122.3899', '9q8yyuwqsxsp');
INSERT INTO locations (created_at, updated_at, id, driver, latitude, longitude, geo_hash_zone) VALUES ('2023-10-07 09:36:01.293512', '2023-10-07 09:36:01.293512', '8bc0ba9b-64ec-11ee-ae9d-47a7287c241b', 'ca27310d-f656-4968-8f22-5dcccefe37f3', '37.7862', '-122.4194', '9q8yyqbbt4pq');
INSERT INTO locations (created_at, updated_at, id, driver, latitude, longitude, geo_hash_zone) VALUES ('2023-10-07 09:36:01.293512', '2023-10-07 09:36:01.293512', '8bc0ba9c-64ec-11ee-ae9d-47a7287c241b', 'ca27310d-f656-4968-8f22-5dcccefe37f3', '37.773', '-122.43', '9q8yyh35wjx3');
INSERT INTO locations (created_at, updated_at, id, driver, latitude, longitude, geo_hash_zone) VALUES ('2023-10-07 09:36:01.293512', '2023-10-07 09:36:01.293512', '8bc0ba9d-64ec-11ee-ae9d-47a7287c241b', 'ca27310d-f656-4968-8f22-5dcccefe37f3', '37.8899', '-122.4588', '9q8zt3shxjk9');
INSERT INTO locations (created_at, updated_at, id, driver, latitude, longitude, geo_hash_zone) VALUES ('2023-10-07 10:40:34.759', '2023-10-07 10:40:34.759', 'c688ae6c-1d83-410c-83ac-0cb1f7858c2f', 'ca27310d-f656-4968-8f22-5dcccefe37f3', '37.773', '-122.43', '9q8yyh35wjx3');
INSERT INTO locations (created_at, updated_at, id, driver, latitude, longitude, geo_hash_zone) VALUES ('2023-10-07 10:40:39.795', '2023-10-07 10:40:39.795', 'ab915994-aea0-4fbd-b8a3-27a4dc5d03a3', 'ca27310d-f656-4968-8f22-5dcccefe37f3', '37.773', '-122.43', '9q8yyh35wjx3');
INSERT INTO locations (created_at, updated_at, id, driver, latitude, longitude, geo_hash_zone) VALUES ('2023-10-07 13:53:04.978', '2023-10-07 13:53:04.978', 'af009725-0c13-44e9-ac8d-284fd72ce08b', 'ca27310d-f656-4968-8f22-5dcccefe37f3', '37.773', '-122.43', '9q8yyh35wjx3');
INSERT INTO locations (created_at, updated_at, id, driver, latitude, longitude, geo_hash_zone) VALUES ('2023-10-07 13:53:54.911', '2023-10-07 13:53:54.911', '204bc49b-8fd1-4b5d-80a4-7b30147f5eeb', 'ca27310d-f656-4968-8f22-5dcccefe37f3', '37.773', '-122.43', '9q8yyh35wjx3');


--
-- Data for Name: last_known_locations; Type: TABLE DATA; Schema: theride; Owner: theride
--

INSERT INTO last_known_locations (created_at, updated_at, id, driver, location) VALUES ('2023-10-07 10:26:30.087901', '2023-10-07 13:53:54.971', '98faf434-64f3-11ee-ae9d-47a7287c241b', 'ca27310d-f656-4968-8f22-5dcccefe37f3', '204bc49b-8fd1-4b5d-80a4-7b30147f5eeb');