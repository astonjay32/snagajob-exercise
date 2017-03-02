## Snagajob Coding Exercise
Spring Boot app which demonstrates the ability to accept posted applications that satisfy a list of acceptable answers.

## Live Demo
http://snagajobexercise-env.eu-west-1.elasticbeanstalk.com/

## Running Locally
Run the following Maven command to start the application on port 8080:
```
mvn spring-boot:run
```

## API endpoints (JSON)
- GET `/applications` - Lists all accepted applications.
- GET `/application/1` - List a single application.
- GET `/applications/questions` - Lists all the questions and required answers.
- POST `/applications` - Add a new application.  Response code *201* means application was accepted, *204* means rejected.
- GET `/applications/deleteall` - Deletes all applications.

## Acceptable Answers
Defined by [JSON file](src/main/resources/application-questions.json) which describes the questions in the application and its accetable answer.
```json
[
    {
    "id": "1",
    "question": "Do you like to work?",
    "answer": "yes"
    },
    {
    "id": "2",
    "question": "Are you allowed to?",
    "answer": "yes"
    },
    {
    "id": "3",
    "question": "Are you a fugitive?",
    "answer": "no"
    }
]
```

