TRUNCATE TABLE users;

-- Insert dummy users for backend testing
INSERT INTO users (username, password_hash, email) VALUES
('tester', '123', 'tester@example.com'),
('user1', '456', 'user1@example.com'),
('user2', '789', 'user2@example.com');