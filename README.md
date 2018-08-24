DB runner utility.

To ensure your system is ready for utility usage type in command line "java --version"
if you see output like java **.** Java(TM) - your system is setup, otherwise install Java at your system

--- Folder structure ---
runner.jar - main application
dbconnection.properties - DB connection settings
runner_lib - folder with JDBC drivers for necessary DB
db_jobs - folder with properties files for DB execution tasks - 1 property file - 1 SQL statement with defined input parameters and expected output
csv_output - folder with db_jobs execution results in CSV format. !!!This folder will be created by utility if doesn't exist

--- USAGE ---
1. Create properties file in db_jobs folder for example test1.properties and define following keys
   db_job_id=test1  - THIS VALUE MUST BE UNICQUE FOR SUITE
   db_job_query=SELECT * FROM sakila.customer where sakila.customer.store_id = ? AND sakila.customer.last_name = ?
   db_job_query_params=2;SCOTT
   db_job_output_header=film_id,title,description,last_update
2. Navigate to folder where utility located using command line
3. Launch utility using command "java -jar runner.jar"
4. Your results will be in csv_ouput folder or errors reported in command line window
5. csv_output folder will have <timestamp>_suite_execution_report.csv summary report of execution with statuses and errors(if any) and folder <timestamp>_csv_files with result sets of SQL execution
 