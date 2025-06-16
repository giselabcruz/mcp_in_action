This project implements a simple calculator service using **Model Context Protocol (MCP)** with **Spring Boot** and **WebFlux**, serving MCP tools via SSE (Server-Sent Events).

It is designed for educational and practical exploration of MCP tool registration, service transport, and integration with the **MCP Inspector**.

# 🚀 Step-by-Step Setup

## 1. ✅ Initialize the Project with Spring Initializr

Go to [https://start.spring.io](https://start.spring.io) and fill in the fields as follows:

* **Project**: Maven
* **Language**: Java
* **Spring Boot**: 3.2.5 (or later)
* **Group**: `com.example`
* **Artifact**: `calculator`
* **Name**: `calculator`
* **Packaging**: Jar
* **Java version**: 17
* **Dependencies**:

  * Spring Reactive Web (**WebFlux**)
  * Spring Boot Actuator

Click **Generate**, unzip the project, and open it in IntelliJ IDEA.

> \[!NOTE]
> Spring Initializr adds only stable, official dependencies available in Maven Central. Experimental or early-access libraries like MCP are not included by default.

---

## 2. ✅ Add the MCP Dependency Manually

Edit the `pom.xml` file and add the following dependency:

```xml
<dependency>
  <groupId>org.springframework.ai</groupId>
  <artifactId>spring-ai-mcp-server-webflux-spring-boot-starter</artifactId>
  <version>1.0.0-M6</version>
</dependency>
```

> \[!IMPORTANT]
> This dependency is **not included automatically** in Spring Initializr, as it is part of a newer project (Spring AI). You must add it **manually** to enable MCP capabilities.

> \[!TIP]
> Version `1.0.0-M6` is available directly from Maven Central, so no custom repository is needed.

After adding, **sync Maven** in IntelliJ (or run `./mvnw clean install`).

Also, you have to include these dependencies:
```
<dependencies>
  <!-- Web server and reactive stack -->
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webflux</artifactId>
  </dependency>

  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
  </dependency>

  <!-- MCP WebFlux Server -->
  <dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-mcp-server-webflux-spring-boot-starter</artifactId>
  </dependency>

  <!-- Tool annotation support -->
  <dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-tools</artifactId>
  </dependency>

  <!-- Optional: for chatbot features -->
  <dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-client-chat</artifactId>
  </dependency>

  <!-- Testing -->
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
  </dependency>
  <dependency>
    <groupId>io.projectreactor</groupId>
    <artifactId>reactor-test</artifactId>
    <scope>test</scope>
  </dependency>
</dependencies>
```
---

## 3. ✅ Create the Configuration File

Go to `src/main/resources` and open (or create) the file `application.properties`, then paste:

```properties
spring.application.name=calculator
spring.ai.mcp.server.transport=sse
```

> \[!IMPORTANT]
> This tells Spring Boot to enable the MCP server and expose it via SSE at the `/sse` endpoint.

> \[!WARNING]
> If you forget this, the Inspector will connect but won’t find any tools, or `/sse` will return 404.

---

## 4. ✅ Create the MCP Tool Class

In `src/main/java/com/example/calculator/`, create a file named `CalculatorTool.java` with the following content:

```java
package com.example.calculator;

import org.springframework.ai.mcp.tool.Tool;
import org.springframework.stereotype.Component;

@Component
public class CalculatorTool {

    public CalculatorTool() {
        System.out.println("✅ CalculatorTool loaded");
    }

    @Tool
    public double add(double a, double b) {
        return a + b;
    }

    @Tool
    public double subtract(double a, double b) {
        return a - b;
    }

    @Tool
    public double multiply(double a, double b) {
        return a * b;
    }

    @Tool
    public double divide(double a, double b) {
        if (b == 0) throw new IllegalArgumentException("Cannot divide by zero");
        return a / b;
    }
}
```

> \[!CAUTION]
> Make sure this class is in the **same package or subpackage** as your main application class. Otherwise, Spring will not detect it.

---

## 5. ✅ Build and Run the Project

From the terminal inside the project root:

```bash
./mvnw clean install -U
./mvnw spring-boot:run
```

Alternatively:

```bash
./mvnw package -DskipTests
java -jar target/calculator-0.0.1-SNAPSHOT.jar
```

> \[!TIP]
> Use `-U` to force Maven to update dependencies in case it cached a failed lookup earlier.

---

# 🛠️ Use MCP Inspector

## 1. Install and Launch Inspector

```bash
npx @modelcontextprotocol/inspector
```

## 2. Copy Proxy Session Token

In the terminal where Inspector started, look for a line like:

```
Proxy Session Token: abcd1234-efgh5678-ijkl9012
```

Copy that token.

> \[!IMPORTANT]
> If you don’t use the exact session token provided at runtime, the Inspector will connect but return an error or say that no tools are available.

## 3. Configure the Inspector UI

In the browser (usually at `http://localhost:6274`):

* Transport Type: `SSE`
* URL: `http://localhost:8080/sse`
* Open `Authentication` section

  * **Proxy Session Token**: paste the token you copied
  * **Bearer Token**: leave empty
* Click **Connect**

> \[!CAUTION]
> Don’t leave the Proxy Session Token empty — even locally — or the Inspector will fail to list your tools.

---

## 4. Run Tools

* Click **List Tools**
* Select a method like `calculatorTool.add`
* Provide input JSON:

```json
{
  "a": 5,
  "b": 3
}
```

* Click **Run Tool**

You should get:

```json
{
  "result": 8
}
```

> \[!NOTE]
> Tool names are based on the Java class name in camel case (`CalculatorTool` becomes `calculatorTool`).

---

# 💡 Notes, Tips & Cautions

### 🛠️ Debugging:

* If Inspector connects but no tools are listed:

  * Ensure your tool class is annotated with `@Component`
  * Ensure methods are annotated with `@Tool`
  * Ensure package is scanned by Spring Boot

* If `/sse` gives 404:

  * Check `application.properties` contains `spring.ai.mcp.server.transport=sse`

> \[!WARNING]
> Do not rename or move `CalculatorTool.java` outside your base package. Spring won’t scan it.

### 🧪 Test endpoint manually:

```bash
curl http://localhost:8080/sse
```

### ✅ Dev trick:

Print a line in your tool class constructor to verify it's loaded:

```java
System.out.println("✅ CalculatorTool loaded");
```

