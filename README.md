# Connect Four

A two-player Connect Four game built with Kotlin/JS and Compose HTML.

## Features

- Configurable board size (2×2 up to 20×20)
- Configurable win condition (Connect 2 up to Connect 10)
- Two-player local gameplay with gravity rules
- Win and draw detection with winning cells highlighted
- Piece drop animation
- Game state saved to localStorage — resume after browser refresh
- Responsive layout for desktop and mobile

## Tech stack

- Kotlin 2.1.20 (JS/IR target)
- Compose HTML 1.7.3
- Gradle 8.10

## Run locally

```bash
./gradlew jsBrowserDevelopmentRun
```

Opens at `http://localhost:8080`.

## Run tests

```bash
./gradlew jsTest
```

## Build for production

```bash
./gradlew jsBrowserDistribution
```

Output in `build/dist/js/productionExecutable/`.

## Live demo

http://connect-four-game-gold.vercel.app/

## Author

Taipova Evgeniya
