# samples-db-containers

Sample of configuring GitHub Containers for test databases:
- Always on containers for Postgres, Oracle y SQL Server
- Init script to create each test database and user
- Environment variables to store credentials, with fallback to a properties file
- Test database access from Java

## Configure your local development environment

- Define the credentials to access each database:
  - either set the environment variables `TEST_POSTGRES_PWD`, `TEST_ORACLE_PWD` and `TEST_SQLSERVER_PWD`
  - or set their assignments in a file `setup/environment.properties` as pairs `name=value`
    (this file is included in .gitignore to avoid storing credentials in the remote repo)
- Start containers by running the script at `setup/container-setup.sh`
- Run the tests with `mvn test`
