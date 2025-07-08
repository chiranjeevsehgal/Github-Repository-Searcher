# GitHub Repository Searcher API

Spring Boot REST API for searching GitHub repositories and managing repository data with database persistence.

# Tech Stack

- **Java 21**
- **Spring Boot 3.5.3**
- **PostgreSQL**
- **Spring Data JPA for database operations**
- **GitHub API integration**
- **WebClient for HTTP requests**
- **Maven - Build tool**
- **JUnit 5 + Mockito - Testing**
- **H2 Database - Testing**


## Setup & Run

1. **Database Setup**
   ```sql
   CREATE DATABASE github_searcher;
   ```

2. **Configure Application:**
   Update `application.properties` with your credentials:
   ```properties
    spring.datasource.username=your_postgres_username
    spring.datasource.password=your_postgres_password
   ```

3. **Run Application**
   ```bash
   mvn spring-boot:run
   ```
   API runs on: `http://localhost:8081`

## API Documentation

### 1. Search Repositories
**Endpoint:** `POST /api/github/search`  
**URL:** `http://localhost:8081/api/github/search`

**Request:**
```json
{
    "query": "spring boot",
    "language": "Java",
    "sort": "stars"
}
```

**Response:**
```json
{
    "message": "Repositories fetched and saved successfully",
    "repositories": [
        {
            "id": 127988011,
            "name": "mall",
            "description": "mall项目是一套电商系统，包括前台商城系统及后台管理系统，基于Spring Boot+MyBatis实现，采用Docker容器化部署。 前台商城系统包含首页门户、商品推荐、商品搜索、商品展示、购物车、订单流程、会员中心、客户服务、帮助中心等模块。 后台管理系统包含商品管理、订单管理、会员管理、促销管理、运营管理、内容管理、统计报表、财务管理、权限管理、设置等模块。",
            "owner": "macrozheng",
            "language": "Java",
            "stars": 80954,
            "forks": 29360,
            "lastUpdated": "2025-07-08T16:34:27"
        },
        {
            "id": 6296790,
            "name": "spring-boot",
            "description": "Spring Boot helps you to create Spring-powered, production-grade applications and services with absolute minimum fuss.",
            "owner": "spring-projects",
            "language": "Java",
            "stars": 77692,
            "forks": 41269,
            "lastUpdated": "2025-07-08T14:56:03"
        },
        {
            "id": 9754983,
            "name": "tutorials",
            "description": "Getting Started with Spring Boot 3: ",
            "owner": "eugenp",
            "language": "Java",
            "stars": 37194,
            "forks": 54228,
            "lastUpdated": "2025-07-08T15:52:45"
        },
        ......
    ]
}
```

### 2. Get Stored Repositories
**Endpoint:** `GET /api/github/repositories`  
**URL:** `http://localhost:8081/api/github/repositories`

**Query Parameters:**
- `language` (optional): Filter by programming language
- `minStars` (optional): Filter by minimum star count
- `sort` (optional): Sort by 'stars', 'forks', or 'updated' (default: 'stars')

**Example:**
```
GET /api/github/repositories?sort=stars&minStars=100&language=Java
```

**Response:**
```json
{
    "repositories": [
        {
            "id": 127988011,
            "name": "mall",
            "description": "mall项目是一套电商系统，包括前台商城系统及后台管理系统，基于Spring Boot+MyBatis实现，采用Docker容器化部署。 前台商城系统包含首页门户、商品推荐、商品搜索、商品展示、购物车、订单流程、会员中心、客户服务、帮助中心等模块。 后台管理系统包含商品管理、订单管理、会员管理、促销管理、运营管理、内容管理、统计报表、财务管理、权限管理、设置等模块。",
            "owner": "macrozheng",
            "language": "Java",
            "stars": 80954,
            "forks": 29360,
            "lastUpdated": "2025-07-08T16:34:27"
        },
        {
            "id": 6296790,
            "name": "spring-boot",
            "description": "Spring Boot helps you to create Spring-powered, production-grade applications and services with absolute minimum fuss.",
            "owner": "spring-projects",
            "language": "Java",
            "stars": 77692,
            "forks": 41269,
            "lastUpdated": "2025-07-08T14:56:03"
        },
        {
            "id": 9754983,
            "name": "tutorials",
            "description": "Getting Started with Spring Boot 3: ",
            "owner": "eugenp",
            "language": "Java",
            "stars": 37194,
            "forks": 54228,
            "lastUpdated": "2025-07-08T15:52:45"
        },
        ....
    ]
}
```

## Testing
```bash
mvn test
```

## Features
- Search repositories using GitHub's REST API
- Stores repository data in PostgreSQL 
- Support for language filtering, star count filtering, and multiple sort options
