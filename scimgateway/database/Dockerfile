FROM postgres:latest
env POSTGRES_PASSWORD example
env POSTGRES_USER postgres
env POSTGRES_DB postgres
COPY init.sql /docker-entrypoint-initdb.d/10-init.sql