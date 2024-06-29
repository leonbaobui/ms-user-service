create sequence users_seq start 1 increment 1;

DROP TABLE IF EXISTS users, user_blocked, user_follower_requests,
    subscribers, user_muted, user_subscriptions;

create table users (
    id                    int8 not null,
    about                 varchar(255),
    activation_code       varchar(255),
    active                boolean default false,
    avatar                varchar(255),
    background_color      varchar(255) default 'DEFAULT',
    color_scheme          varchar(255) default 'BLUE',
    birthday              varchar(255),
    country               varchar(255),
    country_code          varchar(255),
    email                 varchar(255) unique not null,
    full_name             varchar(255) not null,
    gender                varchar(255),
    language              varchar(255),
    like_count            int8    default 0,
    location              varchar(255),
    media_tweet_count     int8    default 0,
    muted_direct_messages boolean default false,
    notifications_count   int8    default 0,
    mentions_count        int8    default 0,
    password              varchar(255),
    password_reset_code   varchar(255),
    phone                 int8,
    pinned_tweet_id       int8,
    private_profile       boolean default false,
    profile_customized    boolean default false,
    profile_started       boolean default false,
    registration_date     timestamp default current_timestamp,
    role                  varchar(255) default 'USER',
    tweet_count           int8    default 0,
    unread_messages_count int8    default 0,
    username              varchar(255) not null,
    wallpaper             varchar(255),
    website               varchar(255),
    created_at            timestamp not null default current_timestamp,
    updated_at            timestamp not null default current_timestamp,
    primary key (id)
);

create table subscribers
(
    user_id       int8 not null,
    subscriber_id int8 not null
);

create table user_blocked (
    user_id         int8 not null,
    blocked_user_id int8 not null
);

create table user_follower_requests
(
    user_id     int8 not null,
    follower_id int8 not null
);
create table user_muted
(
    user_id       int8 not null,
    muted_user_id int8 not null
);
create table user_subscriptions
(
    subscriber_id int8 not null,
    user_id       int8 not null
);
alter table subscribers
    add constraint fk_subscribers_id_subscribers foreign key (subscriber_id) references users (id);
alter table subscribers
    add constraint fk_user_id_subscribers foreign key (user_id) references users (id);
alter table user_blocked
    add constraint fk_blocked_user_id_user_blocked foreign key (blocked_user_id) references users(id);
alter table user_blocked
    add constraint fk_user_id_user_blocked foreign key (user_id) references users(id);
alter table user_follower_requests
    add constraint fk_follower_id_user_follower_requests foreign key (follower_id) references users(id);
alter table user_follower_requests
    add constraint fk_user_id_user_follower_requests foreign key (user_id) references users(id);
alter table user_muted
    add constraint fk_muted_user_id_user_muted foreign key (muted_user_id) references users(id);
alter table user_muted
    add constraint fk_user_id_user_muted foreign key (user_id) references users(id);
alter table user_subscriptions
    add constraint fk_user_id_user_subscriptions foreign key (user_id) references users(id);
alter table user_subscriptions
    add constraint fk_subscriber_id_user_subscriptions foreign key (subscriber_id) references users(id);