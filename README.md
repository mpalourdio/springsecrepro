# Bug Repo

- Run DemoApplication
- Go to http://localhost:8080/api/ping
- Look at Stack Trace : `Cannot create a session after the response has been committed`

# Fix (?)

- Uncomment line 33 in `ApplicationSecurityConfig`. That was no needed in the very last Spring Boot 2 for example. (Sometimes forcing IF_REQUIRED works too !)
