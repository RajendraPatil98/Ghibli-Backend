# 🎨 Ghibli Art AI - AI-Powered Anime Art Generation API

A Spring Boot REST API that generates stunning Studio Ghibli-style anime artwork using Stability AI's state-of-the-art image generation models. Transform plain images or text prompts into beautiful, detailed anime art with the artistic style of Studio Ghibli.

---

## 📋 Table of Contents

- [Features](#-features)
- [Architecture](#-architecture)
- [Tech Stack](#-tech-stack)
- [Prerequisites](#-prerequisites)
- [Installation & Setup](#-installation--setup)
- [Configuration](#-configuration)
- [API Endpoints](#-api-endpoints)
- [Usage Examples](#-usage-examples)
- [Project Structure](#-project-structure)
- [Key Implementation Details](#-key-implementation-details)
- [Error Handling](#-error-handling)
- [Future Enhancements](#-future-enhancements)
- [Contributing](#-contributing)

---

## ✨ Features

- **Text-to-Image Generation**: Convert text descriptions into stunning anime artwork
- **Image-to-Image Style Transfer**: Transform existing images with Ghibli-style anime aesthetic
- **Automatic Image Resizing**: Handles image optimization for API compatibility
- **RESTful API Design**: Clean, well-documented endpoints with proper HTTP standards
- **CORS Support**: Cross-origin requests enabled for frontend integration
- **Spring Cloud OpenFeign Integration**: Declarative HTTP client for seamless API communication
- **Error Handling**: Comprehensive exception handling with meaningful error responses
- **API Documentation**: Built-in Swagger UI for interactive API exploration
- **Production-Ready**: Java 17, Spring Boot 3.5.0, Maven-based project structure

---

## 🏗️ Architecture

The application follows a **layered architecture pattern** with clear separation of concerns:

```
┌─────────────────────────────────────────────────┐
│         REST Controller Layer                   │
│    (GenerationController)                       │
│  • Request Validation                           │
│  • Response Formatting                          │
└──────────────┬──────────────────────────────────┘
               │
┌──────────────▼──────────────────────────────────┐
│         Service Layer                           │
│    (GhibliArtService)                           │
│  • Business Logic                               │
│  • Prompt Engineering                           │
│  • Image Processing                             │
└──────────────┬──────────────────────────────────┘
               │
┌──────────────▼──────────────────────────────────┐
│         Feign Client Layer                      │
│    (StabiltyAIClient)                           │
│  • HTTP Communication                           │
│  • API Integration                              │
│  • Request/Response Transformation              │
└──────────────┬──────────────────────────────────┘
               │
┌──────────────▼──────────────────────────────────┐
│    Stability AI API (External Service)          │
│  https://api.stability.ai/                      │
└─────────────────────────────────────────────────┘
```

### Design Patterns Used:

1. **Dependency Injection**: Constructor-based DI for loose coupling
2. **Client-Server Pattern**: RESTful communication model
3. **Facade Pattern**: Service layer abstracts complex operations
4. **Decorator Pattern**: Prompt engineering adds context to user inputs
5. **Strategy Pattern**: Multiple style presets for different artistic styles

---

## 🛠️ Tech Stack

| Component | Version | Purpose |
|-----------|---------|---------|
| **Java** | 17 | Base programming language |
| **Spring Boot** | 3.5.0 | Application framework |
| **Spring Cloud OpenFeign** | 2025.0.0 | Declarative HTTP client |
| **Spring Web** | 3.5.0 | REST controller support |
| **SpringDoc OpenAPI** | 2.5.0 | API documentation & Swagger UI |
| **Project Lombok** | 1.18.38 | Boilerplate code reduction |
| **Maven** | 3.x+ | Build and dependency management |
| **Apache Tomcat** | 10.x | Embedded servlet container |

---

## 📋 Prerequisites

- **Java Development Kit (JDK)**: Version 17 or higher
- **Maven**: Version 3.6.0 or higher
- **Git**: For version control
- **Stability AI API Key**: Obtain from [stability.ai](https://stability.ai)
- **Internet Connection**: For API communication

### Optional:
- **Postman/Insomnia**: For API testing
- **VS Code / IntelliJ IDEA**: IDE for development

---

## 📦 Installation & Setup

### Step 1: Clone the Repository

```bash
git clone https://github.com/yourusername/ghibli-ai.git
cd ghibliapi
```

### Step 2: Verify Java Installation

```bash
java -version
# Output should show Java 17 or higher
```

### Step 3: Build the Project

```bash
# Using Maven
mvn clean install

# Or using Maven wrapper (if present)
./mvnw clean install
```

### Step 4: Configure Environment Variables

Create `src/main/resources/application.properties`:

```properties
# Stability AI Configuration
stability.api.key=your_stability_ai_api_key_here
stability.api.base-url=https://api.stability.ai

# Server Configuration
server.port=8082
server.servlet.context-path=/

# Logging Configuration
logging.level.root=INFO
logging.level.in.rajendrapatil.ghibliapi=DEBUG

# OpenAPI/Swagger Configuration
springdoc.swagger-ui.enabled=true
springdoc.api-docs.path=/v3/api-docs
```

### Step 5: Run the Application

```bash
# Using Maven
mvn spring-boot:run

# Or using Java directly after build
java -jar target/ghibliapi-0.0.1-SNAPSHOT.jar
```

### Step 6: Verify Application Startup

```
2025-11-24T01:18:53.591+05:30  INFO 13512 --- [main] 
c.i.r.g.GhibliapiApplication : Started GhibliapiApplication in 2.347 seconds
```

Access the application at: `http://localhost:8082`

---

## ⚙️ Configuration

### Stability AI API Keys

1. Visit [Stability AI Dashboard](https://platform.stability.ai/account/keys)
2. Create a new API key
3. Add to `application.properties`:

```properties
stability.api.key=sk-xxxxxxxxxxxxxxxxxxxx
```

### CORS Configuration

The application supports CORS for frontend integration:

```java
@CrossOrigin(origins = {
    "http://localhost:5173",      // Vite default port
    "http://127.0.0.1:5173"        // Localhost alternative
})
```

To add more origins, modify the `@CrossOrigin` annotation in `GenerationController.java`.

### Server Port Configuration

```properties
# Default port: 8082
server.port=8082
```

---

## 🔌 API Endpoints

### 1. Text-to-Image Generation

**Endpoint**: `POST /api/v1/generate-from-text`

**Description**: Generates anime artwork from text description

**Request Body**:
```json
{
  "prompt": "A mystical forest with glowing trees",
  "style": "anime"
}
```

**Available Styles**:
- `anime` - General anime style
- `neon-punk` - Cyberpunk neon aesthetic
- `3d-model` - 3D rendered look
- `digital-art` - Digital painting style

**Response**:
- **Success (200)**: Binary PNG image data
- **Error (400)**: Bad request
- **Error (500)**: Server error

**Example using cURL**:
```bash
curl -X POST http://localhost:8082/api/v1/generate-from-text \
  -H "Content-Type: application/json" \
  -d '{
    "prompt": "A beautiful castle in the clouds",
    "style": "anime"
  }' \
  --output generated_image.png
```

### 2. Image-to-Image Style Transfer

**Endpoint**: `POST /api/v1/generate`

**Description**: Transforms an existing image with Ghibli-style anime aesthetic

**Request Parameters**:
- `image` (multipart/form-data, required): Image file (PNG, JPG)
- `prompt` (string, required): Additional description to guide the transformation

**Response**:
- **Success (200)**: Binary PNG image data
- **Error (400)**: Bad request (missing image)
- **Error (500)**: Server error

**Example using cURL**:
```bash
curl -X POST http://localhost:8082/api/v1/generate \
  -F "image=@path/to/image.jpg" \
  -F "prompt=Transform this into beautiful anime art" \
  --output transformed_image.png
```

**Example using JavaScript Fetch API**:
```javascript
const formData = new FormData();
formData.append('image', fileInput.files[0]);
formData.append('prompt', 'Beautiful anime transformation');

const response = await fetch('http://localhost:8082/api/v1/generate', {
  method: 'POST',
  body: formData
});

const imageBlob = await response.blob();
const imageUrl = URL.createObjectURL(imageBlob);
```

### 3. API Documentation

**Endpoint**: `GET /swagger-ui.html`

Interactive API documentation powered by Swagger UI. View all endpoints, try them out, and see real-time responses.

---

## 💡 Usage Examples

### Example 1: Generate Anime Art from Text (Python)

```python
import requests
import json

url = "http://localhost:8082/api/v1/generate-from-text"

payload = {
    "prompt": "A girl with long hair standing on a cliff overlooking mountains",
    "style": "anime"
}

headers = {
    "Content-Type": "application/json"
}

response = requests.post(url, json=payload, headers=headers)

if response.status_code == 200:
    with open("output.png", "wb") as f:
        f.write(response.content)
    print("Image generated successfully!")
else:
    print(f"Error: {response.status_code}")
    print(response.text)
```

### Example 2: Transform Image with Ghibli Style (JavaScript/React)

```javascript
import React, { useState } from 'react';

function GhibliArtGenerator() {
  const [image, setImage] = useState(null);
  const [prompt, setPrompt] = useState('');
  const [loading, setLoading] = useState(false);
  const [generatedImage, setGeneratedImage] = useState(null);

  const handleGenerateArt = async () => {
    if (!image || !prompt) {
      alert('Please provide both image and prompt');
      return;
    }

    setLoading(true);
    const formData = new FormData();
    formData.append('image', image);
    formData.append('prompt', prompt);

    try {
      const response = await fetch('http://localhost:8082/api/v1/generate', {
        method: 'POST',
        body: formData
      });

      if (response.ok) {
        const blob = await response.blob();
        setGeneratedImage(URL.createObjectURL(blob));
      } else {
        alert('Failed to generate art');
      }
    } catch (error) {
      console.error('Error:', error);
      alert('Error generating art');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      <input
        type="file"
        accept="image/*"
        onChange={(e) => setImage(e.target.files[0])}
      />
      <textarea
        value={prompt}
        onChange={(e) => setPrompt(e.target.value)}
        placeholder="Describe the transformation..."
      />
      <button onClick={handleGenerateArt} disabled={loading}>
        {loading ? 'Generating...' : 'Generate Ghibli Art'}
      </button>
      {generatedImage && <img src={generatedImage} alt="Generated" />}
    </div>
  );
}

export default GhibliArtGenerator;
```

### Example 3: Batch Generation (Node.js)

```javascript
const axios = require('axios');
const fs = require('fs');

async function generateBatchArt() {
  const prompts = [
    "A magical forest with glowing flowers",
    "An ancient temple in the mountains",
    "A girl flying through clouds"
  ];

  for (let i = 0; i < prompts.length; i++) {
    try {
      const response = await axios.post(
        'http://localhost:8082/api/v1/generate-from-text',
        {
          prompt: prompts[i],
          style: "anime"
        },
        { responseType: 'arraybuffer' }
      );

      fs.writeFileSync(`output_${i + 1}.png`, response.data);
      console.log(`Generated image ${i + 1} successfully`);
    } catch (error) {
      console.error(`Error generating image ${i + 1}:`, error.message);
    }
  }
}

generateBatchArt();
```

---

## 📁 Project Structure

```
ghibliapi/
├── src/
│   ├── main/
│   │   ├── java/in/rajendrapatil/ghibliapi/
│   │   │   ├── GhibliapiApplication.java          # Spring Boot entry point
│   │   │   ├── controller/
│   │   │   │   └── GenerationController.java      # REST API endpoints
│   │   │   ├── service/
│   │   │   │   └── GhibliArtService.java          # Business logic layer
│   │   │   ├── client/
│   │   │   │   └── StabiltyAIClient.java          # Feign HTTP client
│   │   │   ├── config/
│   │   │   │   └── FeignConfig.java               # Feign configuration
│   │   │   ├── dto/
│   │   │   │   ├── TextGenerationRequestDTO.java  # Request payload
│   │   │   │   └── TextToImageRequest.java        # API request object
│   │   │   └── util/
│   │   │       └── ImageResizingUtil.java         # Image processing utilities
│   │   └── resources/
│   │       ├── application.properties             # Configuration file
│   │       └── static/                            # Static assets
│   └── test/
│       └── java/
│           └── ...Tests.java                      # Unit tests
├── pom.xml                                         # Maven configuration
├── README.md                                       # This file
└── HELP.md                                         # Additional help

```

---

## 🔍 Key Implementation Details

### 1. **Spring Cloud OpenFeign Integration**

The `StabiltyAIClient` interface uses declarative HTTP client capabilities:

```java
@FeignClient(
    name = "stabilityAiClient",
    url = "${stability.api.base-url}",
    configuration = FeignConfig.class
)
public interface StabiltyAIClient {
    // Declarative method signatures
}
```

**Benefits**:
- Reduces boilerplate HTTP code
- Automatic serialization/deserialization
- Built-in error handling
- Spring integration

### 2. **Feign Configuration for Multipart Form Data**

The `FeignConfig.java` implements custom encoder to support multipart/form-data:

```java
@Bean
public Encoder feignFormEncoder(HttpMessageConverters httpMessageConverters) {
    return new SpringFormEncoder(new SpringEncoder(httpMessageConverters));
}
```

**Purpose**: Enables file upload capability in Feign clients

### 3. **Image Resizing Utility**

Handles automatic image optimization to meet API requirements:

```
Original Image → Validation → Resizing (if needed) → API Call
```

**Features**:
- Checks image dimensions
- Compresses if exceeds limits
- Maintains aspect ratio
- Supports multiple formats (PNG, JPG)

### 4. **Prompt Engineering Strategy**

Adds contextual styling to user prompts:

```java
// User prompt: "A magical forest"
// Final prompt: "A magical forest, in the beautiful, detailed anime style of studio ghibli."
String finalPrompt = prompt + ", in the beautiful, detailed anime style of studio ghibli.";
```

**Rationale**: Ensures consistent Ghibli aesthetic across generations

### 5. **Request-Response Transformation**

**TextToImageRequest Structure**:
```json
{
  "text_prompts": [
    { "text": "prompt text" }
  ],
  "cfg_scale": 7,
  "height": 1024,
  "width": 1024,
  "samples": 1,
  "steps": 30,
  "style_preset": "anime"
}
```

Maps user-friendly requests to Stability AI API format.

---

## ⚠️ Error Handling

### Common Error Scenarios

| Error | Cause | Solution |
|-------|-------|----------|
| `400 Bad Request` | Missing or invalid parameters | Verify request body/parameters |
| `401 Unauthorized` | Invalid/missing API key | Check `stability.api.key` configuration |
| `400 - text_prompts: cannot be blank` | Empty prompt in TextToImageRequest | Ensure prompt is populated before API call |
| `500 Internal Server Error` | Server-side exception | Check logs and API limits |
| `ConnectionException` | Cannot reach Stability AI API | Verify internet connection and API URL |

### Error Response Format

```json
{
  "error": {
    "message": "Descriptive error message",
    "timestamp": "2025-11-24T01:18:53Z",
    "status": 400
  }
}
```

### Debugging Tips

1. **Enable Debug Logging**:
```properties
logging.level.in.rajendrapatil.ghibliapi=DEBUG
logging.level.org.springframework.cloud.openfeign=DEBUG
```

2. **Check Application Logs**:
```bash
tail -f logs/application.log
```

3. **Verify API Key**:
```bash
curl -H "Authorization: Bearer YOUR_API_KEY" \
  https://api.stability.ai/v1/engines/list
```

---

## 🚀 Future Enhancements

1. **Database Integration**
   - Store generation history
   - User account management
   - Generation statistics

2. **Advanced Features**
   - Multiple style presets (Miyazaki, Takahata, etc.)
   - Negative prompts for fine-grained control
   - Batch generation API
   - Real-time WebSocket updates for long-running generations

3. **Performance Optimization**
   - Request caching layer
   - Image compression
   - Asynchronous processing with message queues (RabbitMQ/Kafka)

4. **Monitoring & Analytics**
   - Integration with Prometheus/Grafana
   - Request rate limiting
   - API usage analytics dashboard

5. **Authentication & Security**
   - JWT-based authentication
   - API key management
   - Rate limiting per user
   - HTTPS/TLS enforcement

6. **Testing**
   - Comprehensive unit tests
   - Integration tests
   - Load testing with JMeter
   - Mock Stability AI client for offline testing

---

## 🤝 Contributing

Contributions are welcome! Please follow these guidelines:

1. **Fork the repository**
2. **Create a feature branch**: `git checkout -b feature/your-feature`
3. **Make your changes**
4. **Write/update tests**
5. **Commit with clear messages**: `git commit -m "Add: your feature description"`
6. **Push to branch**: `git push origin feature/your-feature`
7. **Create a Pull Request**

---

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

---

## 📞 Support & Contact

- **Issues**: Report bugs via GitHub Issues
- **Email**: rajendrapatil@example.com
- **Documentation**: Full API docs available at `/swagger-ui.html`

---

## 🙏 Acknowledgments

- **Stability AI**: For providing powerful image generation API
- **Studio Ghibli**: For inspiring the artistic direction
- **Spring Boot Community**: For excellent framework and documentation
- **OpenFeign**: For declarative HTTP client capabilities

---

## 📊 Project Statistics

- **Language**: Java 17
- **Framework**: Spring Boot 3.5.0
- **Lines of Code**: ~500
- **API Endpoints**: 2 (+ Swagger documentation)
- **External APIs**: 1 (Stability AI)
- **Build Tool**: Maven

---

## 🎯 Interview Talking Points

### Technical Highlights:

1. **Microservices Architecture**: Clean separation of concerns using controller-service-client pattern

2. **Spring Cloud Integration**: Expert use of OpenFeign for declarative HTTP clients with custom configuration

3. **API Integration**: Proper handling of multipart/form-data, custom encoders, and request transformation

4. **Error Handling**: Comprehensive exception handling with user-friendly error responses

5. **Configuration Management**: Externalized configuration using properties files for environment flexibility

6. **Design Patterns**: Implementation of Facade, Decorator, and Strategy patterns

7. **RESTful Principles**: Proper HTTP methods, status codes, and content negotiation

8. **Dependency Injection**: Constructor-based DI using Spring's IoC container

9. **CORS & Security**: Proper cross-origin configuration for frontend integration

10. **Documentation**: Self-documenting API with Swagger UI integration

### Problem-Solving Skills:

- Resolved Spring Boot 3.5.0 vs 4.0.0 compatibility issues with `HttpMessageConverters`
- Implemented custom Feign encoder for multipart form data support
- Optimized image handling with automatic resizing and compression

---

**Last Updated**: November 24, 2025

**Version**: 1.0.0

**Status**: Production Ready ✅
