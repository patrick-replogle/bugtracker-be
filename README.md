# Bugtracker

## Project Overview
An application to help developers keep track of bugs across different projects. 

Basic features include: 
- user onboarding and authentication using OAUTH
- The ability to create a project and add additional users to that project
- Create and assign tickets for each project based on priority level and mark tickets as completed
- Comment on tickets

Deployed site: https://bugtracker-frontend.netlify.app

Backend base url: https://bugtracker-back-end.herokuapp.com

Front-end repo: https://github.com/patrick-replogle/bugtracker-fe

## Tech Stack
 - Java
 - Spring Boot 
 - OAUTH
 - Maven
 - H2
 - PostgreSQL
 - Heroku
 - Vue
 - Nuxt
 - Bootstrap
 
 ## Contributor
 
 |[Patrick Replogle](https://github.com/patrick-replogle) |                                                                                                                                                                    
 | :----------------------------------------------------------------------------------------------------------------------------------------------------------------------:|
 | [<img src="https://avatars2.githubusercontent.com/u/50844285?s=400&u=7ffa88c4c221bf888b1771fec72530ac156d90c6&v=4" width = "200" />](https://github.com/patrick-replogle) |
 |   [<img src="https://github.com/favicon.ico" width="15"> ](https://github.com/patrick-replogle) |
 |  [ <img src="https://static.licdn.com/sc/h/al2o9zrvru7aqj8e1x2rzsrca" width="15"> ](https://www.linkedin.com/in/patrick-replogle-409a92193/)|
 
 ## Required Environment Variables
 - RUNTIME_ENV (development / production / testing / staging / etc...)
 - OAUTHCLIENTID
 - OAUTHCLIENTSECRET
 - POSTGRES_USER
 - POSTGRES_PASSWORD
 - POSTGRES_URL
 
 ## API Routes
 
 ### Auth endpoints
 | http type |            endpoint            |      category               | payload |
 | --------- | :----------------------------: | --------------------------: | -------:|
 | post      |       /auth/register           | register a new user         |     yes |
 | post      |       /login                   | login user                  |     yes |
 | get       |       /logout                  | logout authenticated user   |     yes |
 
 ### User endpoints
 | http type |            endpoint                             |      category                | payload |
 | --------- | :---------------------------------------------: | --------------------------:  | -------:|
 | get       |         /users/users                            |   get all users              |      no |
 | get       |         /users/user/:userid                     |   get user by id             |      no |
 | get       |         /users/name/:username                   |   get user by username       |      no |
 | delete    |         /users/user/:userid                     |   delete user                |      no |
 | patch     |         /users/user/:userid                     |   update a user              |     yes |
 | post      |         /users/user/:userid/project/:projectid  |   join a project             |      no |
 | delete    |         /users/user/:userid/project/:projectid  |   remove user from project   |      no |
 
 ### Project endpoints
  | http type |            endpoint                           |      category                | payload |
  | --------- | :--------------------------------------------: | --------------------------: | -------:|
  | get       |      /projects/projects                        |   get all projects          |      no |
  | get       |      /projects/project/:projectid              |   get project by id         |      no |
  | post      |      /projects/projects                        |   create new project        |      yes|
  | patch     |      /projects/project/:projectid              |   update a project          |      yes|
  | delete    |      /projects/project/:projectid              |   delete project by id      |      no |
  | post      |      /projects/project/:projectid/user/:userid |   add user to project       |      yes|
  | delete    |      /projects/project/:projectid/user/:userid |   remove user from project  |      no |
  
  ### Ticket endpoints
  | http type |            endpoint                 |      category            | payload |
  | --------- | :---------------------------------: | -----------------------: | -------:|
  | get       |      /tickets/ticket/:ticketid      |  get ticket by id        |      no |
  | post      |      /tickets/ticket                |  create new ticket       |     yes |
  | patch     |      /tickets/ticket/:ticketid      |  update a ticket         |     yes |
  | delete    |      /tickets/ticket/:ticketid      |  delete a ticket         |      no |
  
 ### Comment endpoints
 | http type |            endpoint               |      category            | payload |
 | --------- | :-------------------------------: | -----------------------: | -------:|
 | get       |    /comments/comment/:commentid   |  get comment by id       |      no |
 | post      |    /comments/comment              |  create new comment      |     yes |
 | patch     |    /comments/comment/:commentid   |  update a comment        |     yes |
 | delete    |    /comments/comment/:commendid   |  delete a comment        |      no |

