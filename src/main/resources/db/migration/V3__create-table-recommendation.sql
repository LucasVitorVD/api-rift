CREATE TABLE recommendations (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(100) NOT NULL,
    category_id INT NOT NULL,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(100),
    preview_url VARCHAR(255),
    image VARCHAR(255),
    personal_comment VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (category_id) REFERENCES categories(id)
);