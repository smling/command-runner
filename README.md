# CommandRunner ⚙️

Small Java utility for running external system commands with a configurable timeout, capturing output and exit codes in a simple API.

## Features ✨

| Feature              | Description                                         |
|----------------------|-----------------------------------------------------|
| ✅ Simple API        | Fluent builder to configure timeout                 |
| 🕒 Timeout handling  | Forcibly kills long‑running processes               |
| 📜 Captured output   | Stdout/stderr merged into a single `String`         |
| 🧪 Tested            | JUnit 5 parameterized tests for multiple scenarios  |

## Getting Started ▶️

### Requirements

- JDK 21 (or compatible)
- Gradle wrapper (included in this repo)

### Build & Test Locally

```bash
./gradlew clean build   # compile + run tests
./gradlew test          # run tests only
```

The built JAR will be in `build/libs/`.

## Usage Example 💻

```java
import io.github.smling.command.CommandRunner;
import io.github.smling.command.ExecuteResult;

import java.time.Duration;
import java.util.List;

public class Example {
    public static void main(String[] args) {
        CommandRunner runner = CommandRunner.builder()
                .duration(Duration.ofSeconds(5))
                .build();

        ExecuteResult result = runner.run("ls", List.of("-al"));

        System.out.println("Exit code: " + result.exitCode());
        System.out.println("Output:\n" + result.result());
    }
}
```

On Windows you might prefer:

```java
ExecuteResult result = runner.run("dir", List.of("/w"));
```

## Dependencies 📦

This project uses:

- Java standard library only (no external runtime dependencies)
- Gradle 8 via the included wrapper
- JUnit 5 for tests (`testImplementation` only)

### Using CommandRunner as a dependency

If you publish this project to a Maven repository (for example GitHub Packages), the coordinates will be:

- **Group:** `io.github.smling`
- **Artifact:** `command-runner`
- **Version:** (from `build.gradle`, e.g. `1.1.1`)

**Gradle (Kotlin DSL):**

```kotlin
implementation("io.github.smling:command-runner:1.1.1")
```

**Maven:**

```xml
<dependency>
  <groupId>io.github.smling</groupId>
  <artifactId>command-runner</artifactId>
  <version>1.1.1</version>
</dependency>
```

Alternatively, you can download the JAR from the GitHub Releases page and put it directly on your application’s classpath.

## Contributing 🤝

1. Fork the repository and clone your fork.
2. Create a feature branch: `git checkout -b feature/my-change`.
3. Make your changes, keeping to the existing style (`io.github.smling.command` package, 4-space indentation).
4. Run the checks locally:
   ```bash
   ./gradlew clean test
   ./gradlew check
   ```
5. Open a pull request with:
   - A clear description of the change and rationale.
   - Any relevant usage examples.

CI will automatically build, test, scan dependencies, and run static/secret checks on your PR. 🎯

## CI / CD 🚀

This repository uses GitHub Actions (see `.github/workflows/`):

- **CI (`ci.yml`)** – runs on pushes and pull requests to `main`/`master`:
  - Build (`./gradlew assemble`)
  - Unit tests (`./gradlew test`)
  - Style/static checks (`./gradlew check`)
  - CodeQL security scan
  - Dependency review and OWASP Dependency-Check (using `NVD_API_KEY` secret)
  - Secret scanning via Gitleaks  
  Test and scan reports are uploaded as workflow artifacts.

- **CD (`cd.yml`)** – runs on pushes to `main`:
  - Builds the app (`./gradlew clean build`)
  - Reads the version from `build.gradle`
  - Creates a Git tag and GitHub Release using that version
  - Uploads the built JAR from `build/libs/` as a release asset

Update the Gradle `version` in `build.gradle` before merging to `main` to control release tags.
