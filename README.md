# crypto-api
crypto assesment


## Postgress tables
```
create table users(
    id SERIAL PRIMARY KEY,
    username TEXT NOT NULL,
    password TEXT NOT NULL,
    created DATE NOT NULL,
    modified DATE NOT NULL
)
```
