#!/bin/bash

# Container setup for local development environment

# Define the credentials to access each database:
#  - either set the environment variables `TEST_POSTGRES_PWD`, `TEST_ORACLE_PWD` and `TEST_SQLSERVER_PWD`
#  - or set their assignments in a file `setup/environment.properties` as pairs `name=value`
#    (this file is included in .gitignore to avoid storing credentials in the remote repo)

# ensures this script is running in his folder
SCRIPT_DIR=$(readlink -f $0 | xargs dirname)
echo "run command at directory: $SCRIPT_DIR"
cd $SCRIPT_DIR

# Required environment variables can be set in this file
source ./environment.properties

echo "Postgres setup for local environment"
# Note the / in the volume bind, required when running under some windows shells (e.g. mingw)
docker stop test-postgres && docker rm test-postgres
docker run -d -p 5432:5432 --name test-postgres  --restart unless-stopped \
  -e POSTGRES_PASSWORD="$TEST_POSTGRES_PWD" \
  -e TEST_POSTGRES_PWD="$TEST_POSTGRES_PWD" \
  -v /${PWD}/postgres:/docker-entrypoint-initdb.d \
  postgres:14
./wait-container-ready.sh test-postgres "END SETUP!"

echo "Oracle setup for local environment"
docker stop test-oracle && docker rm test-oracle
docker run -d -p 1521:1521 --name test-oracle  --restart unless-stopped \
  -e ORACLE_PASSWORD=$TEST_ORACLE_PWD \
  -e TEST_ORACLE_PWD="$TEST_ORACLE_PWD" \
  -v /${PWD}/oracle:/container-entrypoint-initdb.d \
  gvenzl/oracle-xe:11.2.0.2-slim-faststart
./wait-container-ready.sh test-oracle "DATABASE IS READY TO USE!"

#docker exec -it test-oracle sqlplus sampledb/${TEST_ORACLE_PWD}@XE
