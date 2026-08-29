# Springa

Springa · your habits, beautifully organized.

Built autonomously by **AppFactory**.

## Product
- Idea: A habit tracker for drinking water every day
- Archetype: studio
- Entity: Habit (Habits)
- Application id: com.springa.i8lj
- Version: 1.0.0 (1)
- Permissions: none (fully offline)

## Reliability
- JVM unit tests run in CI (`testDebugUnitTest`).
- Signed APK produced and verified in GitHub Actions; then verified again by AppFactory against the release artifact.

## Build
Push to GitHub and GitHub Actions will build, test, sign and verify the release APK automatically. No local Android tooling is required for users.