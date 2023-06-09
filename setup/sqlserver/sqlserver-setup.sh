# Execution of postgres commands at startup
# note the first line specifying the shel is removed to avoid provelem with line breaks in windows
echo "-- Begin setup"
/opt/mssql-tools/bin/sqlcmd -S localhost,1433 -U sa -P $TEST_SQLSERVER_PWD   <<-EOSQL
  CREATE LOGIN sampledb WITH PASSWORD = '$TEST_SQLSERVER_PWD', CHECK_POLICY=OFF, CHECK_EXPIRATION=OFF, DEFAULT_LANGUAGE=spanish;
  GO
  CREATE DATABASE sampledb
  GO
  USE [sampledb]
  CREATE USER [sampledb] FOR LOGIN [sampledb] --WITH DEFAULT_SCHEMA=[dbo]
  EXEC sp_addrolemember 'db_owner', 'sampledb' 
  GO
EOSQL
echo "-- END SETUP!"