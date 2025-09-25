#!/bin/bash
echo "🎬 Starting Movie Management System..."
echo "📊 Checking prerequisites..."

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "❌ Maven not found. Please install Maven first."
    exit 1
fi

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "❌ Java not found. Please install Java 11 or higher."
    exit 1
fi

echo "✅ Prerequisites check passed"
echo "🚀 Compiling and launching application..."

# Compile and run
mvn clean compile exec:java -Dexec.mainClass="com.mms.Main"

echo "🏁 Application finished."
