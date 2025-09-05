GRADLEW = ./gradlew

.PHONY: run
run:
	@$(GRADLEW) bootRun

.PHONY: build
build:
	@$(GRADLEW) build

.PHONY: clean
clean:
	@$(GRADLEW) clean

.PHONY: test
test:
	@$(GRADLEW) test

.PHONY: test-integration
test-integration:
	@$(GRADLEW) integrationTest

.PHONY: test-all
test-all:
	@$(GRADLEW) check