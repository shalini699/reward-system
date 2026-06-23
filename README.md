# Rewards API

A Spring Boot REST API that calculates customer reward points based on transaction amounts.

## Reward Rules

- No points for purchases up to $50.
- 1 point for every dollar spent between $50 and $100.
- 2 points for every dollar spent over $100.

## Example
(e.g. a $120 purchase = 2x$20 + 1x$50 = 90 points).

| Amount | Points |

|----------|----------|

| $120   |   90 |

| $180   |   210 |

| $200   |   250 |

### Technology Used:
- Java 21
- Spring Boot 3.5
- MySQL
- Swagger
- JUnit 5
- Mockito

### Architecture
The application follows Layered Architecture:

Controller → Service → Repository → Database

### API Endpoints:
- GET /api/rewards

- GET /api/rewards/customers/{customerId}

- GET /api/rewards/customers/name/{name}


### Example Response 
Response body-[
  {
    "customerId": 1,
    "customerName": "John",
    "monthlyRewards": [
      {
        "monthYear": "2026-01",
        "rewardPoints": 115
      },
      {
        "monthYear": "2026-02",
        "rewardPoints": 250
      }
    ],
    "totalRewards": 365
  },
  {
    "customerId": 2,
    "customerName": "Mary",
    "monthlyRewards": [
      {
        "monthYear": "2026-01",
        "rewardPoints": 150
      }
    ],
    "totalRewards": 150
  }
]

## Run the Application
mvn spring-boot:run

## Build the Application
mvn clean install

### Swagger:
http://localhost:8082/swagger-ui/index.html




