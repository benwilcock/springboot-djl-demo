# Spring Boot DJL Demo

Welcome to the **Spring Boot DJL Demo** project! This documentation will guide you through the setup, usage, and features of this project.

## Overview

This project demonstrates how to integrate [Deep Java Library (DJL)](https://djl.ai/) with [Spring Boot](https://spring.io/projects/spring-boot) to build and serve machine learning models in Java applications.

## Features

- Load and serve deep learning models using DJL
- RESTful API endpoints for inference
- Easy integration with Spring Boot applications

## Prerequisites

- Java 11 or higher
- Maven 3.6+
- Docker (optional, for containerization)

## Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/benwilcock/springboot-djl-demo.git
cd springboot-djl-demo
```

### 2. Build the Project

```bash
mvn clean package
```

### 3. Run the Application

```bash
java -jar target/springboot-djl-demo-*.jar
```

The application will start on [http://localhost:8080](http://localhost:8080).

## API Usage

### Example Endpoint

- **POST** `/predict`
  - Accepts: Image or data payload (see API docs)
  - Returns: Prediction result

#### Sample Request

```bash
curl -X POST -F 'file=@image.jpg' http://localhost:8080/predict
```

#### Sample Response

```json
{
  "prediction": "cat",
  "probability": 0.98
}
```

## Configuration

You can configure model paths and other settings in `application.properties`.

## Troubleshooting

- Ensure Java and Maven are installed and on your PATH.
- Check logs for errors if the application fails to start.

## Contributing

Contributions are welcome! Please open issues or submit pull requests.

## License

This project is licensed under the MIT License.

---

For more information, visit the [DJL documentation](https://djl.ai/docs/) or [Spring Boot documentation](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/).