# ScheduMAsters

## Description

This is a Spring Boot application that acts as a Scheduling Service that streamlines and optimizes meeting
times for all team members.

## Table of Contents

- [Configuration](#configuration)
- [Installation](#installation)
- [Usage](#usage)

## Configuration

Make sure to replace the ```application.yml``` and ```docker-compose.yml``` with your config data.

## Installation

Provide instructions on how to install and run your application. Include any dependencies or prerequisites.

```shell
# Example installation steps
git clone https://github.com/nderimsali096/ScheduMasters.git
cd your-repo
mvn spring-boot:run
```

Alternatively you can execute the main method in the SoftScheduGardenApplication.java class from your IDE.


## Usage

To use this Scheduling Service, follow these steps:

1. **Register as a Team Member**:

   Send a POST request to `/api/auth/register` with a JSON body containing the following information:

   ```json
   {
       "name": "Your Name",
       "email": "your.email@example.com",
       "password": "yourpassword",
       "role": "USER"
   }
  ``

2. **Register as a Team Member**:

   Send a POST request to `/api/auth/authenticate` with a JSON body containing the following information:

   ```json
   {
       "email": "your.email@example.com",
       "password": "yourpassword"
   }
   ```

   1. **Get The most optimized meeting slot**:

      Send a POST request to `/api/meeting/find-optimal-slot` with a JSON body containing the following information:

      ```json
      {
          "availability": {
             "Adrian": ["Monday 14:00-16:00", "Tuesday 09:00-11:00"],
             "Johanna": ["Monday 15:00-17:00", "Wednesday 10:00-12:00"],
             "Sebastian": ["Tuesday 09:00-11:00", "Wednesday 11:00-13:00"]
          }
      }
   ```
