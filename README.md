## User Endpoints:
- /users/register | Returns valid user object and stores in MySQL database correctly 
- /users/login | Returns valid JWT token and stores in Redis database correctly
- /users/logout | Returns 200 status code and removes JWT token from Redis database correctly
- /users/points/add/:id | Returns 200 status code and adds points to user correctly
- /users/points/remove/:id | Returns 200 status code and removes points from user correctly
- /users/points/:id | Returns 200 status code and returns user points correctly
- /users/points/delete/:id | Returns 200 status code and deletes user correctly
## Business Endpoints:
- /business/create | Returns valid business object and stores in MySQL database correctly
- /business/delete | Returns 200 status code and deletes business correctly