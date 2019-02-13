# assignmentRepo
Camel JSON to CSV conversion

Camel REST api that takes a JSON file and carries it through a camel route
Processes in camel route remove events from JSON file and convert it to CSV format
Batches every 10 records or after 60 seconds.

!USE REST DSL PROJECT!
To use project, send JSON project to : http://localhost:8080/api/convert
Files for unit test are in resources folder