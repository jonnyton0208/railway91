-- Reset database
DELETE FROM certificate;
DELETE FROM lesson;
DELETE FROM course;
DELETE FROM category;
DELETE FROM account;
ALTER TABLE account AUTO_INCREMENT = 1;
ALTER TABLE category AUTO_INCREMENT = 1;

-- Insert default categories
INSERT INTO category (name) VALUES 
('Programming'),
('Web Development'),
('Mobile Development'),
('DevOps'),
('Database'); 