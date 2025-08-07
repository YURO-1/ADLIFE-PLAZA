# ADLIFE PLAZA - Medical Management System

A modern, professional medical management system built with JavaFX and socket-based client-server architecture.

## 🏥 Overview

ADLIFE PLAZA is a comprehensive medical management system designed for healthcare facilities. It provides separate interfaces for receptionists and doctors, with real-time data synchronization through socket communication.

## ✨ Features

### 🎨 Modern UI Design
- **Professional Interface**: Modern card-based design with glass morphism effects
- **Responsive Layout**: Adapts to different screen sizes
- **Medical Theme**: Professional color scheme suitable for healthcare environments
- **Accessibility**: High contrast and readable fonts

### 👩‍⚕️ Receptionist Features
- **Patient Registration**: Add new patients with full details
- **Patient Search**: Search existing patients by name or ID
- **Queue Management**: View and manage patient queue
- **Status Updates**: Mark patients as seen or remove from queue

### 👨‍⚕️ Doctor Features
- **Patient Queue**: View current patient queue
- **Medical History**: Document and save patient medical history
- **Consultation Management**: Begin consultations and update patient status
- **Real-time Updates**: Live queue updates

### 🔧 Technical Features
- **Socket Communication**: Real-time client-server communication
- **Multi-threaded Servers**: Concurrent handling of multiple clients
- **Data Persistence**: CSV-based data storage
- **Error Handling**: Comprehensive error handling and user feedback

## 🚀 Quick Start

### Prerequisites
- Java 11 or higher
- JavaFX (included with Java 11+)

### Installation & Setup

1. **Clone the repository**:
   ```bash
   git clone <repository-url>
   cd ADLIFE-PLAZA
   ```

2. **Build the project**:
   ```bash
   ./gradlew build
   ```

3. **Start the system**:
   ```bash
   # Windows
   start_servers.bat
   
   # Or manually start servers and application
   ```

### Manual Server Startup

1. **Start Login Server** (Port 5000):
   ```bash
   java -cp "app/build/classes/java/main" org.example.Server.LoginServer
   ```

2. **Start Receptionist Server** (Port 6000):
   ```bash
   java -cp "app/build/classes/java/main" org.example.Server.ReceptionistServer
   ```

3. **Start Doctor Server** (Port 5555):
   ```bash
   java -cp "app/build/classes/java/main" org.example.Server.DoctorServer
   ```

4. **Start Application**:
   ```bash
   java -cp "app/build/classes/java/main" --add-modules javafx.controls,javafx.fxml org.example.App
   ```

## 🔐 Test Credentials

### Receptionist Login
- **Username**: `#rcpMary`
- **Password**: `rcpPass123`

### Doctor Login
- **Username**: `#dctAdrian`
- **Password**: `dctPass456`

## 🏗️ System Architecture

### Client-Server Architecture
```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   JavaFX App    │    │  Receptionist   │    │   Doctor        │
│   (Client)      │    │   Server        │    │   Server        │
│                 │    │   (Port 6000)   │    │   (Port 5555)   │
└─────────────────┘    └─────────────────┘    └─────────────────┘
         │                       │                       │
         └───────────────────────┼───────────────────────┘
                                 │
                    ┌─────────────────┐
                    │   Login Server  │
                    │   (Port 5000)   │
                    └─────────────────┘
```

### Data Flow
1. **Authentication**: Login server validates credentials
2. **Patient Management**: Receptionist server handles patient operations
3. **Medical Operations**: Doctor server manages consultations and history
4. **Data Storage**: CSV files for persistent data storage

## 📁 Project Structure

```
ADLIFE-PLAZA/
├── app/
│   ├── src/main/
│   │   ├── java/org/example/
│   │   │   ├── Server/                 # Server implementations
│   │   │   │   ├── LoginServer.java
│   │   │   │   ├── ReceptionistServer.java
│   │   │   │   └── DoctorServer.java
│   │   │   ├── *.java                  # Client classes
│   │   │   └── *.java                  # Data models
│   │   └── resources/
│   │       ├── FXML/                   # UI layouts
│   │       │   ├── LoginScreenUI.fxml
│   │       │   ├── ReceptionistDashboard.fxml
│   │       │   └── DoctorDashboard.fxml
│   │       ├── Styles/                 # CSS styling
│   │       │   └── modern-theme.css
│   │       └── CSV/                    # Data files
│   │           ├── user_details.csv
│   │           ├── patients.csv
│   │           ├── doctor_queue.csv
│   │           └── medical_history.csv
│   └── build.gradle                    # Build configuration
├── start_servers.bat                   # Windows startup script
├── UI_IMPROVEMENTS.md                  # UI documentation
├── RUN_DEMO.md                         # Demo guide
└── README.md                           # This file
```

## 🎨 UI Improvements

The system features a modern, professional interface with:

- **Card-based Design**: Clean, organized content sections
- **Glass Morphism**: Subtle transparency and shadow effects
- **Professional Color Scheme**: Medical-appropriate colors
- **Responsive Layout**: Adapts to different screen sizes
- **Smooth Animations**: Subtle hover effects and transitions

See `UI_IMPROVEMENTS.md` for detailed documentation.

## 🔧 Configuration

### Server Ports
- **Login Server**: 5000
- **Receptionist Server**: 6000
- **Doctor Server**: 5555

### Data Files
- **User Details**: `app/src/main/resources/CSV/user_details.csv`
- **Patients**: `app/src/main/resources/CSV/patients.csv`
- **Queue**: `app/src/main/resources/CSV/doctor_queue.csv`
- **Medical History**: `app/src/main/resources/CSV/medical_history.csv`

## 🛠️ Development

### Building from Source
```bash
./gradlew clean build
```

### Running Tests
```bash
./gradlew test
```

### Adding New Features
1. Create new FXML files in `app/src/main/resources/FXML/`
2. Add corresponding Java controllers
3. Update server implementations for new functionality
4. Update socket communication protocols

## 📊 Data Format

### User Details CSV
```csv
username,password,role
#rcpMary,rcpPass123,Receptionist
#dctAdrian,dctPass456,Doctor
```

### Patients CSV
```csv
firstName,lastName,dob,regDate
John,Doe,1990-05-12,2024-01-15
```

### Queue CSV
```csv
patientName,status
John Doe,Waiting
Jane Smith,In Consultation
```

## 🚨 Troubleshooting

### Common Issues

1. **Port Already in Use**:
   - Check if servers are already running
   - Kill existing processes or change ports

2. **JavaFX Not Found**:
   - Ensure Java 11+ is installed
   - Check JavaFX modules are available

3. **Connection Refused**:
   - Ensure all servers are running
   - Check firewall settings

4. **Build Failures**:
   - Clean and rebuild: `./gradlew clean build`
   - Check Java version compatibility

### Logs
- Server logs are displayed in their respective console windows
- Client logs are shown in the application console

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 👥 Team

- **UI/UX Design**: Modern, professional interface
- **Backend Development**: Socket-based server architecture
- **Frontend Development**: JavaFX client application
- **System Integration**: Comprehensive medical management system

---

**ADLIFE PLAZA** - Linking patients to world class doctors worldwide 🏥