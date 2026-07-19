# trivia-quiz-system-app
Backend service for Trivia quiz application 
handling logic in a communication between the frontend and Trivia Quiz Database Api.

❗ **NOTE**: At this moment the application does not provide any quiz customizability and serves players with only 1 quiz variation of 10 questions of random difficulty, categories and types.

## Run application
### Locally
Follow these steps to be able to run application locally:
1. Have jdk-25 and mvn(latest) installed locally OR use EDI's(e.g. `Intellij`) built in options
2. Run `mvn clean install` OR use EDI's `Maven`-plugin to pull and install dependencies
3. Run `TriviaApplication.class` using `localhost`-profile

### Server

[//]: # TODO: Add link to the instance running on a serven when known

## Architecture
*Install `Mermaid`-plugin to view graph(s) properly.*
### High level process visualization
```mermaid
graph LR
    subgraph " "
        web-app([trivia-quiz-front])
        back-end-service[trivia-quiz-system-app]
    trivia-db-api[(trivia DB - API)]
    web-app -- "GET /questions" --> back-end-service
    web-app -- "POST/checkanswers" --> back-end-service
    back-end-service -- "answer validation results" --> web-app
back-end-service <-- "Fetch quiz questions" --> trivia-db-api
end
```
### Process logic visualization
#### Fetch quiz questions - process
```mermaid
sequenceDiagram
    actor QUIZ PLAYER
    participant TriviaQuizController as TriviaQuizController(REST)
    participant TriviaQuizService as TriviaQuizService
    participant TriviaApiFeignClient as TriviaApiFeignClient

    QUIZ PLAYER->>TriviaQuizController: getQuestions()
    TriviaQuizController->>TriviaQuizService: fetchQuizQuestions()
    Note right of QUIZ PLAYER: Uses WebConfig for CORS-configuration
    TriviaQuizService->>TriviaApiFeignClient: getQuizQuestions()
    Note right of TriviaQuizService: Uses FeignConfig for FeignException decoding and Retryer configuration
    TriviaApiFeignClient-->>TriviaQuizService: TriviaApiResponse
    TriviaQuizService-->>TriviaQuizController: List<QuizQuestionDto>
    Note right of TriviaQuizController: Uses DataOptimizationUtilities for response sanitization
    TriviaQuizController-->>QUIZ PLAYER: 200 OK (QuizQuestions)
```

#### CheckAnswers - process
```mermaid
sequenceDiagram
    actor QUIZ PLAYER
    participant TriviaQuizController as TriviaQuizController(REST)
    participant TriviaQuizService as TriviaQuizService

    QUIZ PLAYER->>TriviaQuizController: validatePlayerAnswers(List<PlayerAnswer>)
    TriviaQuizController->>TriviaQuizService: checkAnswers(List<PlayerAnswers>)
    Note right of QUIZ PLAYER: Uses WebConfig for CORS-configuration
    TriviaQuizService-->>TriviaQuizController: List<PlayerAnswerValidationResponse>
    Note right of TriviaQuizController: Uses DataOptimizationUtilities for response sanitization and response validation
    TriviaQuizController-->>QUIZ PLAYER: 200 OK (List<PlayerAnswerValidationResponse>)
```
