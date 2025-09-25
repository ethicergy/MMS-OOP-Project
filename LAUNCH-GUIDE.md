# 🚀 MMS One-Click Launch Setup

## Quick Start Options

### 1. **One-Click VS Code Launch** (Recommended)
- Press `F5` or `Ctrl+F5` in VS Code
- Or go to Run and Debug panel → Select launch configuration:
  - **🎬 Launch MMS** - Normal application launch
  - **🧪 Launch MMS with Tests** - Run tests then launch UI
  - **🔓 Launch MMS (Skip Login)** - Dev mode, skip login screen
  - **🔬 Debug MMS** - Launch with debugging enabled

### 2. **Shell Scripts** (Linux/Mac)
```bash
# Normal launch
./run.sh

# Development mode (skip login, debug console)
./dev-run.sh

# Test mode (run comprehensive tests first)
./test-run.sh
```

### 3. **Direct Maven Commands**
```bash
# Normal launch
mvn compile exec:java -Dexec.mainClass="com.mms.Main"

# With command line arguments
mvn compile exec:java -Dexec.mainClass="com.mms.Main" -Dexec.args="--skip-login"
mvn compile exec:java -Dexec.mainClass="com.mms.Main" -Dexec.args="--test"
mvn compile exec:java -Dexec.mainClass="com.mms.Main" -Dexec.args="--help"
```

## 🐛 Debug Features

### **Debug Console Window**
- **Automatically opens** in debug mode
- **Real-time logging** of application events
- **Positioned alongside main app** for easy monitoring
- **Color-coded messages** (green text on black background)

### **Command Line Options**
- `--test` - Run comprehensive test suite before UI launch
- `--skip-login` - Skip login screen (debug mode only)
- `--help` - Show usage information

### **Visual Debug Benefits**
- 🔍 **Real-time application flow** tracking
- 📊 **Database operation** monitoring
- 🖱️ **UI interaction** logging
- ❌ **Error tracking** with timestamps
- ✅ **Success confirmation** messages

## 🎯 Development Workflow

1. **Start Development**: `./dev-run.sh` or use VS Code "Skip Login" config
2. **Visual Debug**: Debug console opens automatically
3. **Code Changes**: Make changes and relaunch
4. **Test Everything**: `./test-run.sh` before committing
5. **Final Test**: `./run.sh` for full user experience

## 🎪 What Happens on Launch

1. ✅ **System Check** - Verifies Java and Maven
2. 🔧 **Look & Feel** - Sets system-appropriate UI theme
3. 🐛 **Debug Setup** - Opens debug console (if debug mode)
4. 📡 **Database Test** - Validates PostgreSQL connection
5. 🧪 **Tests** - Runs comprehensive tests (if requested)
6. 🎬 **UI Launch** - Opens login screen or admin dashboard

## 📁 Files Created

- `src/main/java/com/mms/utils/DebugUtils.java` - Debug console utility
- `.vscode/launch.json` - VS Code launch configurations  
- `run.sh`, `dev-run.sh`, `test-run.sh` - Launch scripts
- Enhanced `Main.java` - Streamlined entry point

The application is now ready for **one-click development and debugging**! 🚀
