CREATE TABLE recipe (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255),
    cuisine VARCHAR(100),
    rating FLOAT,
    prep_time INT,
    cook_time INT,
    total_time INT,
    description TEXT,
    serves VARCHAR(50),
    nutrients JSONB
);