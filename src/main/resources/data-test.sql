INSERT INTO persons (id, name, phone, phone_Second, address, children_Number, children_Comments, email, contact_Link) VALUES (1, 'name 1', 1111111111, 22222222222, 'address 1', 2, 'children comments 1', 'mail1@mail.com', 'contack link 1');
INSERT INTO persons (id, name, phone, phone_Second, address, children_Number, children_Comments, email, contact_Link) VALUES (2, 'name 2', 3333333333, 44444444444, 'address 2', 4, 'children comments 2', 'mail2@mail.com', 'contack link 2');

-- TEST BASE SQL request:
-- select ap.contract_id, ap.id, ap.DATE_TIME_CREATED, products.name from applications as ap
--   left JOIN products on products.id = ap.product_id
-- where contract_id = 2 ORDER BY ap.DATE_TIME_CREATED DESC, id DESC LIMIT 1