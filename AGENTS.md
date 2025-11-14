# Repository Guidelines

## Project Structure & Module Organization
- Core Java code lives in `src/main/java/io/github/smling` and subpackages such as `command`.
- The entry point is `Main.java`; reusable logic is split into focused classes (e.g., `CommandRunner`, `ExecuteResult`).
- Add new features under existing packages rather than creating new top-level packages.

## Build, Test, and Development Commands
- `.\gradlew.bat build` – compile sources and run all tests.
- `.\gradlew.bat test` – run the JUnit 5 test suite only.
- `.\gradlew.bat clean` – remove build outputs; run before debugging odd build issues.
- Always use the Gradle wrapper (`gradlew(.bat)`) instead of a global Gradle install.

## Coding Style & Naming Conventions
- Java code, 4-space indentation, no tabs; keep lines reasonably short (~120 chars).
- Classes/interfaces: `PascalCase` (e.g., `ExecuteCommandException`); methods/variables: `camelCase`; constants: `UPPER_SNAKE_CASE`.
- Keep packages under `io.github.smling.command` unless there is a clear new domain.
- Avoid unnecessary static state; prefer passing dependencies via constructors or method parameters.

## Testing Guidelines
- Tests belong in `src/test/java`, mirroring the main package structure.
- Use JUnit 5 (`@Test`, `@ParameterizedTest`) and descriptive names like `run_returnsExitCodeOnSuccess`.
- Add tests for all new behavior and regressions, covering both success and failure paths.
- Run `.\gradlew.bat test` before opening a pull request.

## Commit & Pull Request Guidelines
- Commits: concise, imperative subjects (e.g., `Add timeout to command runner`).
- Group related changes; avoid mixing large refactors with behavioral changes.
- Pull requests should include a short summary, rationale, and usage examples (commands or inputs/outputs).
- Link related issues (e.g., `Fixes #12`) and call out any breaking changes or migration steps.
