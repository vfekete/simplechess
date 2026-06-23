# Building SimpleChess

SimpleChess is a Java Swing desktop application. It has no external
dependencies. The repository includes a standalone [Apache Ant][ant] build
file, so NetBeans is not required.

## Requirements

- JDK 8 or newer
- Apache Ant 1.9 or newer
- A graphical desktop environment for running the application

Confirm that Java and Ant are installed:

```sh
java -version
ant -version
```

On Debian or Ubuntu, they can be installed with:

```sh
sudo apt install default-jdk ant
```

## Build

From the repository root, run:

```sh
ant clean jar
```

The executable JAR is created at:

```text
dist/SimpleChess.jar
```

## Run

Build and run in one command:

```sh
ant run
```

Alternatively, run the built JAR directly:

```sh
java -jar dist/SimpleChess.jar
```

## Other build targets

```sh
ant compile  # Compile sources into build/classes
ant jar      # Compile and create the executable JAR
ant clean    # Remove build/ and dist/
ant -p       # List available targets
```

The application entry point is `MainWindow`.

[ant]: https://ant.apache.org/
