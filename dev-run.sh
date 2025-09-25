#!/bin/bash
echo "🐛 Starting MMS in Development Mode with Debug Console..."
echo "📊 Checking prerequisites..."

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "❌ Maven not found. Please install Maven first."
    exit 1
fi

echo "✅ Prerequisites check passed"
echo "🔓 Launching with login bypass for development..."

# Compile and run with skip login
mvn compile exec:java -Dexec.mainClass="com.mms.Main" -Dexec.args="--skip-login"

echo "🏁 Development session finished."
