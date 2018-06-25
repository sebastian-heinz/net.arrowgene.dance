API
===
This Document describes the REST API of the server.

Table of contents
===
- [Intro](#Intro)
- [Endpoints](#endpoints)
  - [Authentication](#authentication)
  - [Health](#Health)
- [Links](#links)

Intro
===

For any route that requires authentication, 
the JWT obtained from the [Authentication](#authentication)-Route 
needs to be set in the 'Authorization' HTTP-Request header.
```
Format:
Authorization: Bearer <JWT>   
Example:
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJpYXQiOjE0OTY1NjAzMzQsImV4cCI6MTQ5NjU2NzUzNCwic3ViIjoiMiJ9.aXX12AgOLAJXA-MVjCVqOFR9IkdE1OirSBrqg26NzyTDLmzu003PPCiEeXwuMHDCP3KnGRwzsAhwADAN4wjF_g
```

Endpoints
===

### Authentication
Used to obtain a JWT for authentication against other routes.

Request:
```
> POST /api/v1/authentication HTTP/1.1
> Content-Type: application/json
> Content-Length: 74
 {
  "user": "arrow-query",
  "hash": "f1f85f92de554786ef8a62954b5e8e6b"
 }
```

Response:
```
{
	"jwt": "eyJhbGciOiJIUzUxMiJ9.eyJpYXQiOjE0OTY1NjAzMzQsImV4cCI6MTQ5NjU2NzUzNCwic3ViIjoiMiJ9.aXX12AgOLAJXA-MVjCVqOFR9IkdE1OirSBrqg26NzyTDLmzu003PPCiEeXwuMHDCP3KnGRwzsAhwADAN4wjF_g",
	"message": "",
	"statusCode": 200
}
```

### Health


Links
===
- [https://github.com/jwtk/jjwt](https://github.com/jwtk/jjwt)
