# REST API training service

| Task                                                                                                                                                   |  State  |
|--------------------------------------------------------------------------------------------------------------------------------------------------------|:-------:|
| Create/Read/Update/Delete new Participant. Participant has only name.                                                                                  |  done   |
| Create/Read/Update/Delete new Course. Courses have title, start time (date+time), end time (date+time), description and maximum number of participants |  done   |
| Add or remove participants from the Course. It should not be possible to add/remove when Course has ended, or maximum capacity reached.                |  done   |
| View list of participants for the Course                                                                                                               |  done   |
| View list of courses for the Participant                                                                                                               |  done   |
| Simple authentication and authorisation (Basic Auth) for users with roles ADMIN (can doanything) and REPORTING (cannot make any modifications)         |  done   |

## Run the app

    docker-compose up -d

## Swagger available

    http://localhost:8080/swagger-ui/index.html#/

## Auth & Authz

The application implements basic authentication.  
To send requests from swagger (available without auth) use:

| Username  | Password |   Role    |  Available operations  |
|-----------|:--------:|:---------:|:----------------------:|
| admin     |  5ecr3t  |   ADMIN   | GET, POST, PUT, DELETE |
| reporting |  5ecr3t  | REPORTING |          GET           |

## Notes
- For simplicity, compiled the jar on my local machine and pulled it into a docker container. 
- The database is postgres 13, it’s old, but again it’s easier.