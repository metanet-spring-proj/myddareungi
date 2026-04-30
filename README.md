# 🚲 MyTtareungi (Smart Mobility Initiative)

> **Project Presentation (PPT)**
> [View Presentation Slides](https://docs.google.com/presentation/d/10-_CK_ZAR6R9YfNQYVCOAQ_IGlpPpdM6DbAuNDwillc/edit?usp=sharing)

![Java](https://img.shields.io/badge/Java-17-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.13-brightgreen)
![Oracle DB](https://img.shields.io/badge/Oracle-XE%2021c-red)
![MyBatis](https://img.shields.io/badge/MyBatis-3.5.19-black)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-3.1.3-green)
![Docker](https://img.shields.io/badge/Docker-Containerized-blue)
![GitHub Actions](https://img.shields.io/badge/CI-GitHub%20Actions-black)

## Overview

MyTtareungi is a smart mobility dashboard platform designed to analyze Seoul’s 2025 public bicycle (Ttareungi) usage patterns and support strategic operational decision-making.

This project transforms fragmented CSV-based bicycle rental data into an integrated business intelligence dashboard, enabling trend analysis, KPI monitoring, district-level demand visualization, and strategic resource allocation.

---

## 🎯 Project Purpose

### Background

Existing Seoul public bicycle data was stored primarily as raw CSV datasets, making operational insight extraction inefficient.

As public bicycle rentals increased, the need for:

* Data-driven decision-making
* Operational efficiency
* Strategic district analysis
* Dashboard-based policy support

became increasingly important.

---

### Objectives

* Analyze 2025 Ttareungi rental usage patterns
* Identify monthly and yearly trends
* Derive KPIs for strategic planning
* Support district-level resource optimization

---

### Expected Impact

* Faster and more accurate administrative decisions
* Improved operational resource allocation
* Better demand forecasting
* Enhanced smart mobility governance

---

# 🛠 Tech Stack

## Backend

* Java 17
* Spring Boot 3.5.13
* Spring Security 6.5.9
* JWT (JJWT 0.13.0)
* Oracle DB XE 21c
* MyBatis 3.5.19
* Spring Cache
* Spring AOP

---

## Frontend

* Thymeleaf 3.1.3
* HTML5
* CSS3
* JavaScript (AJAX)
* Chart.js 4.5.1
* Leaflet 1.9.4
* html2canvas
* SSE (Server-Sent Events)

---

## DevOps & Tools

* Docker
* Maven
* GitHub Actions (CI)
* Swagger
* JaCoCo
* JUnit 5

---

# ✨ Key Features

## 🔐 Authentication & Security

* Secure Signup / Login
* BCrypt password encryption
* JWT authentication with HttpOnly cookies
* Role-based access control (Admin / User)

---

## 📊 Smart Dashboard Visualization

* Monthly / Weekly rental trend charts
* Age-group demographic analysis
* Rental type analytics
* District-level heatmap visualization using Seoul GeoJSON
* Dashboard PNG export functionality

---

## ⚡ Performance Optimization

* Applied Spring Cache to major dashboard APIs
* Reduced heavy query bottlenecks dramatically
* Achieved major dashboard response speed improvements

---

## 📁 File Management & Real-Time Notifications

* CSV file upload system
* Admin approval / rejection workflow
* Local + DB file tracking
* SSE real-time notification system

---

## 🌍 Internationalization (i18n)

* Korean / English multilingual support
* MessageSource
* CookieLocaleResolver

---

# 👥 Team: 긍정적이조 (Positive Team)

| Name            | Role & Contributions                                                |
| --------------- | ------------------------------------------------------------------- |
| Lim Seok-jun    | Signup, Login, Password Reset, JWT Authentication, CI Stabilization |
| Ahn Hee-won     | DB Optimization, Docker DB, GitHub Actions CI, Admin Mypage         |
| Yoon Yeo-ok     | Dashboard API, Visualization, Spring Cache, AOP, User Mypage        |
| Jeon Chan-hyeok | File Management, SSE Notifications, Leaflet Map Visualization       |

---

# 🚀 Getting Started

## Prerequisites

* Java 17
* Maven
* Oracle DB (Docker recommended)

---

## Installation

### 1. Clone Repository

```bash id="kvn8b4"
git clone https://github.com/metanet-spring-proj/myddareungi.git
cd myddareungi
```

---

### 2. Configure Database

Set Oracle DB properties in:

```text id="f0dh1o"
src/main/resources/application.properties
```

---

### 3. Run Application

```bash id="k2m9xq"
./mvnw spring-boot:run
```

---

## Access Points

### Main Application

```text id="h8p3sr"
http://localhost:8080
```

### Swagger API Documentation

```text id="j7n4we"
http://localhost:8080/swagger-ui.html
```

---

# 📊 Database Architecture

## User & Operations

* USERS
* FILE_UPLOAD
* NOTIFICATION
* SYSTEM_LOG

---

## Dashboard & Aggregation

* BIKE_USAGE_RAW
* BIKE_KPI
* BIKE_STATS
* BIKE_MONTHLY_SUMMARY
* BIKE_DISTRICT_SUMMARY
* BIKE_AGE_GROUP_SUMMARY

---

# 📈 Core Achievements

* Built a full-stack smart mobility dashboard
* Implemented enterprise-grade authentication and authorization
* Optimized dashboard performance with Spring Cache
* Developed district-level strategic visualization
* Enabled public-sector operational intelligence

---

# 🌍 Vision

MyTtareungi aims to transform Seoul’s public bicycle data into a strategic smart mobility intelligence platform through integrated dashboard analytics, operational optimization, and public-sector digital transformation.

---

© 2026 METANET GLOBAL | SMART MOBILITY INITIATIVE
