#!/bin/bash
echo "🧪 Running MMS with Comprehensive Tests..."
echo "📊 This will test all components before launching the UI"

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "❌ Maven not found. Please install Maven first."
    exit 1
fi

echo "✅ Prerequisites check passed"
echo "🔬 Running comprehensive test suite..."

# Compile and run with tests
mvn compile exec:java -Dexec.mainClass="com.mms.Main" -Dexec.args="--test"

echo "🏁 Testing and launch completed."
