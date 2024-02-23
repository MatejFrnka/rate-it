CREATE SEQUENCE IF NOT EXISTS category_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS criteria_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS interests_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS password_reset_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS places_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS users_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE category
(
    id   BIGINT NOT NULL,
    name VARCHAR(25),
    CONSTRAINT pk_category PRIMARY KEY (id)
);

CREATE TABLE criteria
(
    id          BIGINT  NOT NULL,
    name        VARCHAR(25),
    deleted     BOOLEAN NOT NULL,
    interest_id BIGINT,
    CONSTRAINT pk_criteria PRIMARY KEY (id)
);

CREATE TABLE interest_category
(
    category_id BIGINT NOT NULL,
    interest_id BIGINT NOT NULL
);

CREATE TABLE interests
(
    id          BIGINT        NOT NULL,
    name        VARCHAR(25)   NOT NULL,
    description VARCHAR(1000) NOT NULL,
    deleted     BOOLEAN       NOT NULL,
    exclusive   BOOLEAN       NOT NULL,
    image_name  VARCHAR(255),
    CONSTRAINT pk_interests PRIMARY KEY (id)
);

CREATE TABLE likes
(
    app_user_id BIGINT NOT NULL,
    interest_id BIGINT NOT NULL,
    CONSTRAINT pk_likes PRIMARY KEY (app_user_id, interest_id)
);

CREATE TABLE password_reset
(
    id      BIGINT       NOT NULL,
    token   VARCHAR(255) NOT NULL,
    user_id BIGINT,
    CONSTRAINT pk_passwordreset PRIMARY KEY (id)
);

CREATE TABLE place_image_names
(
    place_id    BIGINT NOT NULL,
    image_names VARCHAR(255)
);

CREATE TABLE places
(
    id          BIGINT           NOT NULL,
    name        VARCHAR(25)      NOT NULL,
    description VARCHAR(1000),
    address     VARCHAR(255),
    latitude    DOUBLE PRECISION NOT NULL,
    longitude   DOUBLE PRECISION NOT NULL,
    deleted     BOOLEAN          NOT NULL,
    user_id     BIGINT,
    interest_id BIGINT,
    CONSTRAINT pk_places PRIMARY KEY (id)
);

CREATE TABLE ratings
(
    rating       INT4 NOT NULL,
    app_user_id  BIGINT  NOT NULL,
    place_id     BIGINT  NOT NULL,
    criterion_id BIGINT  NOT NULL,
    CONSTRAINT pk_ratings PRIMARY KEY (app_user_id, place_id, criterion_id)
);

CREATE TABLE reviews
(
    text        VARCHAR(255)                NOT NULL,
    created_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    app_user_id BIGINT                      NOT NULL,
    place_id    BIGINT                      NOT NULL,
    CONSTRAINT pk_reviews PRIMARY KEY (app_user_id, place_id)
);

CREATE TABLE roles
(
    role_type   VARCHAR(255) NOT NULL,
    app_user_id BIGINT       NOT NULL,
    interest_id BIGINT       NOT NULL,
    CONSTRAINT pk_roles PRIMARY KEY (app_user_id, interest_id)
);

CREATE TABLE user_follows
(
    followed_id BIGINT NOT NULL,
    follower_id BIGINT NOT NULL,
    CONSTRAINT pk_user_follows PRIMARY KEY (followed_id, follower_id)
);

CREATE TABLE users
(
    id          BIGINT       NOT NULL,
    username    VARCHAR(25)  NOT NULL,
    email       VARCHAR(255) NOT NULL,
    password    VARCHAR(255) NOT NULL,
    server_role VARCHAR(255) NOT NULL,
    bio         VARCHAR(150),
    CONSTRAINT pk_users PRIMARY KEY (id)
);

ALTER TABLE users
    ADD CONSTRAINT uc_users_email UNIQUE (email);

ALTER TABLE users
    ADD CONSTRAINT uc_users_username UNIQUE (username);

ALTER TABLE criteria
    ADD CONSTRAINT FK_CRITERIA_ON_INTEREST FOREIGN KEY (interest_id) REFERENCES interests (id);

ALTER TABLE likes
    ADD CONSTRAINT FK_LIKES_ON_APP_USER FOREIGN KEY (app_user_id) REFERENCES users (id);

ALTER TABLE likes
    ADD CONSTRAINT FK_LIKES_ON_INTEREST FOREIGN KEY (interest_id) REFERENCES interests (id);

ALTER TABLE password_reset
    ADD CONSTRAINT FK_PASSWORDRESET_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE places
    ADD CONSTRAINT FK_PLACES_ON_INTEREST FOREIGN KEY (interest_id) REFERENCES interests (id);

ALTER TABLE places
    ADD CONSTRAINT FK_PLACES_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE ratings
    ADD CONSTRAINT FK_RATINGS_ON_APP_USER FOREIGN KEY (app_user_id) REFERENCES users (id);

ALTER TABLE ratings
    ADD CONSTRAINT FK_RATINGS_ON_CRITERION FOREIGN KEY (criterion_id) REFERENCES criteria (id);

ALTER TABLE ratings
    ADD CONSTRAINT FK_RATINGS_ON_PLACE FOREIGN KEY (place_id) REFERENCES places (id);

ALTER TABLE reviews
    ADD CONSTRAINT FK_REVIEWS_ON_APP_USER FOREIGN KEY (app_user_id) REFERENCES users (id);

ALTER TABLE reviews
    ADD CONSTRAINT FK_REVIEWS_ON_PLACE FOREIGN KEY (place_id) REFERENCES places (id);

ALTER TABLE roles
    ADD CONSTRAINT FK_ROLES_ON_APP_USER FOREIGN KEY (app_user_id) REFERENCES users (id);

ALTER TABLE roles
    ADD CONSTRAINT FK_ROLES_ON_INTEREST FOREIGN KEY (interest_id) REFERENCES interests (id);

ALTER TABLE interest_category
    ADD CONSTRAINT fk_intcat_on_category FOREIGN KEY (category_id) REFERENCES category (id);

ALTER TABLE interest_category
    ADD CONSTRAINT fk_intcat_on_interest FOREIGN KEY (interest_id) REFERENCES interests (id);

ALTER TABLE place_image_names
    ADD CONSTRAINT fk_place_imagenames_on_place FOREIGN KEY (place_id) REFERENCES places (id);

ALTER TABLE user_follows
    ADD CONSTRAINT fk_usefol_on_followed FOREIGN KEY (followed_id) REFERENCES users (id);

ALTER TABLE user_follows
    ADD CONSTRAINT fk_usefol_on_follower FOREIGN KEY (follower_id) REFERENCES users (id);