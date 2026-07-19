# Execution of postgres commands at startup
# note the first line specifying the shel is removed to avoid provelem with line breaks in windows
echo "-- Begin setup"
psql -v ON_ERROR_STOP=1   <<-EOSQL
  -- Each role owns its database: since PostgreSQL 15 the public schema no longer
  -- grants CREATE to PUBLIC, so the connecting role must own the DB to create objects
  CREATE USER sampledb with encrypted password '$TEST_POSTGRES_PWD';
  CREATE DATABASE sampledb OWNER sampledb;
EOSQL
echo "-- END SETUP!"