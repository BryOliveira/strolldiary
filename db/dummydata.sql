TRUNCATE TABLE users;

-- Insert dummy users for backend testing
INSERT INTO users (username, password_hash, email) VALUES
('tester', '$2y$10$drYHBhlidhLFTPBzDdoMHeZ78KELdzRangWfxkZV9LEdc50m1yM3q', 'tester@example.com'),
('user1', '$2y$10$5WtbZOlInGBswMdc6SmQ5.YgGXyOVrVXIAlSQpP/O7kG53oLUdKam', 'user1@example.com'),
('user2', '$2y$10$1nfuObCdMQT2WyL7ZhAolu4QSBLo00bkTWIxVthbxZ2e6XoEe/CGW', 'user2@example.com');