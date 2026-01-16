#!/bin/bash

# Stop the application if it's running
echo "Stopping any running instances..."
pkill -f "gradle.*bootRun" 2>/dev/null || true

# Clean all build artifacts
echo "Cleaning build artifacts..."
cd /Users/momarm45/IdeaProjects/jobTracker
rm -rf build/
rm -rf .gradle/

# Rebuild the project
echo "Rebuilding project..."
./gradlew clean build --no-daemon

# Start the application
echo "Starting application..."
./gradlew bootRun --no-daemon
