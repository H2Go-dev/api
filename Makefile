.PHONY: help build test run clean style-check smoke-test docker-build docker-run docker-stop

help:
	@echo "Available commands:"
	@echo "  help           - Show this help message"
	@echo "  build          - Build the project"
	@echo "  test           - Run unit tests (WIP)"
	@echo "  run            - Run the application locally"
	@echo "  clean          - Clean build artifacts"
	@echo "  style-check    - Run style checking"
	@echo "  smoke-test     - Run smoke tests with Docker"
	@echo "  docker-build   - Build Docker image"
	@echo "  docker-run     - Run with Docker Compose"
	@echo "  docker-stop    - Stop Docker Compose"

build:
	./mvnw clean compile

test:
	./mvnw test

run:
	./mvnw spring-boot:run

clean:
	./mvnw clean

style-check:
	./mvnw checkstyle:check

docker-build:
	docker build -t h2go:latest .

docker-run:
	docker-compose up -d

docker-stop:
	docker-compose down

smoke-test: docker-build
	@echo "Starting services for smoke testing..."
	docker-compose -f docker-compose.test.yml up -d
	@echo "Waiting for services to be healthy..."
	@timeout 120 bash -c 'until curl -f http://localhost:8081/actuator/health; do sleep 2; done' || \
		(echo "❌ Service failed to start within timeout"; docker-compose -f docker-compose.test.yml down; exit 1)
	@echo "Running smoke tests..."
	@curl -f http://localhost:8081/actuator/health
	@echo "Smoke tests passed!"
	docker-compose -f docker-compose.test.yml down -v


