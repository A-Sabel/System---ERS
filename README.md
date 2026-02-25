# 🎓 System-ERS: Enrollment & Registration System

**Professional-Grade Student Management System for 2-Year Programs**

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Swing](https://img.shields.io/badge/GUI-Swing-blue.svg)](https://docs.oracle.com/javase/tutorial/uiswing/)
[![Status](https://img.shields.io/badge/Status-Production-brightgreen.svg)]()

---

## 📋 Table of Contents

- [Overview](#overview)
- [System Architecture](#system-architecture)
- [Core Features](#core-features)
- [Academic Progression System](#academic-progression-system)
- [Professional Scheduling Engine](#professional-scheduling-engine)
- [Student CRUD Operations](#student-crud-operations)
- [Technical Specifications](#technical-specifications)
- [Installation & Setup](#installation--setup)
- [Usage Guide](#usage-guide)
- [File Structure](#file-structure)
- [Important Notes](#important-notes)

---

## 🌟 Overview

System-ERS is a comprehensive **Enrollment and Registration System** designed for 2-year academic programs. Built with Java Swing, it provides professional-grade tools for student management, course enrollment, grade tracking, academic progression, and intelligent schedule generation.

### Key Capabilities

✅ **Complete Student Lifecycle Management** - From enrollment to graduation  
✅ **Intelligent Schedule Generation** - University-style multi-section support  
✅ **Academic Progression Tracking** - Automated promotion and graduation processing  
✅ **Grade Management** - Score entry, GWA calculation, and Latin Honors determination  
✅ **Conflict Detection** - Triple-validation (Student/Teacher/Room)  
✅ **Course Retake Support** - Track failed courses and retaken courses with latest status  
✅ **Summer Remedial Courses** - Automated filtering of failed/dropped/incomplete courses

---

## 🏗️ System Architecture

### Application Structure

```
StudentCourseTab (Main Application)
├── Course Tab - Course management and curriculum design
├── Student Tab - Student information CRUD operations
├── Section Tab - Section creation and management
├── Score Tab - Grade entry and marksheet management
├── Mark Sheet Tab - Complete academic record viewing
├── Schedule Tab - Visual schedule display and management
└── Admin Controls Tab - Academic progression and administration
    ├── Semester Management - Complete semester and advance academic year
    ├── Year Promotion - Promote 1st Year → 2nd Year students
    ├── Graduation - Process 2nd Year graduations with Latin Honors
    └── Reports - Generate promotion and graduation reports
```

### Data Persistence

All data is persisted in text files with comma-separated format:

- **student.txt** - Student demographic and academic information
- **enrollment.txt** - Course enrollments with detailed per-course status
- **marksheet.txt** - Grade records and GWA calculations
- **courseSubject.txt** - Course definitions with units and prerequisites
- **Schedule.txt** - Schedule assignments (section/room/teacher/time)
- **teachers.txt** - Faculty information and assigned courses
- **rooms.txt** - Facility information (lecture halls and labs)
- **graduates.txt** - Graduation records with Latin Honors
- **academic_calendar.txt** - Current semester and academic year tracking

---

## 🎯 Core Features

### 1. **Professional Layout & UI Optimization**

- **Balanced Screen Distribution**: 480px left panel | 840px right panel (1350px total)
- **Professional Form Alignment**: 130px labels with 280px fields
- **Smooth Scrolling**: Internal panels (450x950) with vertical scroll
- **Enhanced Visual Appeal**: Consistent spacing and modern design

### 2. **Continuous Schedule Block Rendering**

- **Seamless Display**: Multi-hour courses shown as continuous blocks
- **Mathematical Centering**: Course information centered in merged cells
- **Professional Styling**: 50px row height with hidden grid lines
- **High Contrast**: White text on colored course blocks

### 3. **Intelligent Unit Splicing System**

- **Optimal Distribution**: Maximum 3 hours per day per course
- **Flexible Scheduling**: Minimum 1-hour splice duration
- **17 Time Slots**: 8:00 AM to 5:00 PM coverage (Mon-Fri)
- **Smart Algorithm**: Automatically distributes course units across weekdays

### 4. **Mandatory 30-Minute Break Enforcement**

- **Professional Standards**: Academic break requirements enforced
- **Conflict Prevention**: No back-to-back scheduling allowed
- **Enhanced Detection**: Time overlap validation with break buffers
- **Real-time Validation**: Prevents scheduling violations during assignment

### 5. **Trio Conflict Detection System**

- **Student Conflict Prevention**: Detects overlapping enrollments
- **Teacher Double-booking Protection**: Prevents instructor conflicts
- **Room Management**: Eliminates facility double-booking
- **Comprehensive Analysis**: Real-time conflict reporting with detailed logs

### 6. **Academic Validation System**

**GWA-based Unit Limits:**

- **Dean's List (GWA ≤ 1.75)**: Up to 24 units
- **Good Standing (GWA ≤ 2.25)**: Up to 21 units
- **Satisfactory (GWA ≤ 2.75)**: Up to 18 units
- **Probation (GWA > 2.75)**: Up to 15 units

### 7. **Travel Time Calculations**

- **Building-based Logic**: 5 minutes same building, 15 minutes cross-campus
- **Teacher Scheduling**: Considers instructor movement between locations
- **Buffer Integration**: Combines break time + travel time requirements
- **Feasibility Checks**: Prevents impossible scheduling scenarios

### 8. **Course Retake & Remedial Support**

- **Latest Status Tracking**: System tracks latest status for each course
- **Summer Enrollment**: Only shows courses with current FAILED/DROPPED/INC status
- **Retake Filtering**: Passed retakes are removed from available summer courses
- **Graduation Eligibility**: Considers all required courses have PASSED status
- **Latin Honors Disqualification**: Students with any failed history cannot receive honors

---

## 📚 Academic Progression System

### Semester Management

#### Complete Current Semester

- Updates ENROLLED courses to PASSED/FAILED based on marksheet grades
- **Passing Grade**: 1.0-3.0 → PASSED
- **Failing Grade**: 5.0 → FAILED
- **No Grade**: Remains ENROLLED or becomes INC

#### Summer Semester Support

- **6-15 Unit Limit**: Dedicated unit cap for intensive summer courses
- **Failed Course Filtering**: Only FAILED/DROPPED/INC courses appear in dropdown
- **Latest Status Check**: Excludes already-retaken and passed courses
- **Daily Intensive Scheduling**: Monday-Friday continuous scheduling

### Student Promotion (1st Year → 2nd Year)

#### Eligibility Requirements

✅ Currently in 1st Year  
✅ 1st Semester: All courses PASSED (including retakes)  
✅ 2nd Semester: All courses PASSED (including retakes)  
❌ Summer semester: NOT required for promotion

#### Promotion Process

1. **View Promotion Candidates** - Preview eligible vs ineligible students
2. **Bulk Promotion** - Promote all eligible 1st Year students simultaneously
3. **Automatic Update** - Year level changed from "1st Year" to "2nd Year" in student.txt
4. **Confirmation** - Success message shows count of promoted students

#### Retake Consideration

- System checks **latest status** of each course across all enrollments
- Students can be promoted even if they initially failed and then retook courses
- All required semester 1 & 2 courses must have current status = PASSED

### Graduation Processing (2nd Year Only)

#### Graduation Requirements

✅ **Year Level**: Must be 2nd Year student  
✅ **Courses**: All required courses have current status PASSED  
✅ **Units**: Minimum 60 units earned  
✅ **GWA**: ≤ 3.0 (calculated from all enrolled courses)

#### Latin Honors Classification

Students with **perfect academic record** (no FAILED/DROPPED/INC history):

- **🏆 Summa Cum Laude**: GWA 1.00 - 1.20
- **🥈 Magna Cum Laude**: GWA 1.21 - 1.45
- **🥉 Cum Laude**: GWA 1.46 - 1.75

Students with **retaken courses** (any FAILED/DROPPED/INC history):

- **✓ Regular Graduate**: GWA ≤ 3.0, honors = NONE

**Disqualified from graduation**:

- **✗ Not Eligible**: Missing requirements (incomplete courses, insufficient units, GWA > 3.0)

#### Graduation Workflow

1. **View Graduation Candidates** - Preview report with Latin Honors breakdown
2. **Process Graduation** - Bulk process all eligible 2nd Year students
3. **Automatic Actions**:
   - Record moved to graduates.txt with graduation date, GWA, and honors
   - Student status updated to "Graduate" in student.txt
   - Student removed from active enrollment pool
4. **Confirmation** - Success message shows list of graduated students

### Report Generation

#### Promotion Report

- Shows eligible vs ineligible students for selected year level
- Lists student ID and name
- Counts for each category
- Validates completion of both semesters

#### Graduation Report

- Categorizes 2nd Year students by Latin Honors
- Shows GWA for each candidate
- Identifies eligible vs not eligible students
- Displays [Retake] tag for students with failed course history
- Total eligible count

---

## 🗓️ Professional Scheduling Engine

### University-Style Multi-Section Support

#### Room/Teacher-Specific Conflict Detection

- **Previous Limitation**: Blocked all sections if any resource was busy
- **Current Capability**: Multiple sections can coexist at same time with different resources
- **Real-World Example**: 10 sections of CS101 can all start at 8:00 AM Monday using different rooms and teachers

#### Resource Availability System

```java
findAvailableRoom(day, startTime, endTime, isLab, existingSchedules)
// Finds ANY available room for the time slot

findAvailableTeacher(courseID, day, startTime, endTime, existingSchedules)
// Finds ANY available teacher for the time slot
```

#### Monday-First Scheduling Priority

- **University Logic**: Fill Monday completely before moving to other days
- **Student Flexibility**: Maximizes schedule flexibility for students
- **Professional Practice**: Matches real university scheduling behavior

#### Consistent Course Color Mapping

- All sections of same course use same color (CS101-SEC1, CS101-SEC2, etc.)
- Professional visual consistency across schedule displays
- Easy identification of course blocks in schedule view

### Scheduling Features

- **Intelligent Splicing**: Optimal distribution of course hours across week
- **Break Enforcement**: Mandatory 30-minute breaks between classes
- **Conflict Detection**: Triple-validation (Student/Teacher/Room)
- **Travel Time**: Building-based travel time calculations
- **Visual Schedule**: Color-coded continuous block rendering

---

## 👥 Student CRUD Operations

### Create (Add New Student)

**Required Fields**: Student ID, Student Name  
**Optional Fields**: DOB, Gender, Email, Phone, Address, Parents' Info

**Actions**:

1. Validates required fields
2. Checks for duplicate Student ID
3. Creates new Student object with form data
4. Saves to student.txt via StudentFileSaver
5. Refreshes table display
6. Shows success confirmation

### Read (View & Search)

- **Table Display**: All students shown in sortable table
- **Search Functionality**: Filter students by ID/name
- **Auto-populate**: Click row to load student data into form

### Update (Edit Student)

**Actions**:

1. Select student from table (form auto-populates)
2. Modify desired fields
3. Click Update button
4. System validates and checks for ID conflicts
5. Updates student record in memory and file
6. Refreshes display
7. Shows success confirmation

**Field Preservation**: Year Level, Student Type, Subjects Enrolled, and GWA are preserved from original record

### Delete (Remove Student)

**Actions**:

1. Select student from table
2. Click Delete button
3. Student removed from table and students ArrayList
4. Changes saved to student.txt
5. Shows success confirmation

### Additional Features

- **Clear Form**: Reset all fields to default values
- **Age Calculation**: Automatically calculated from Date of Birth
- **Date Picker**: Convenient JSpinner for DOB selection
- **Gender Dropdown**: Male/Female selection
- **Validation**: Ensures data integrity before save operations

---

## 💻 Technical Specifications

### Technologies Used

- **Language**: Java 17
- **GUI Framework**: Swing
- **Architecture**: MVC-inspired file-based persistence
- **Build Tool**: javac (standard JDK compiler)

### Core Classes

#### Main Application

- **StudentCourseTab.java** - Main application window with tabbed interface
- **TestingPanel.java** - Admin controls for academic progression (434 lines)

#### Data Management

- **Student.java** - Student entity with demographic and academic data
- **Enrollment.java** - Enrollment records with per-course status tracking
- **CourseSubject.java** - Course definitions with units and prerequisites
- **Marksheet.java** - Grade records and GWA calculations
- **Teachers.java** - Faculty information
- **Rooms.java** - Facility information
- **Section.java** - Section entity

#### Business Logic

- **AcademicUtilities.java** - Academic progression & scheduling validation (1650+ lines)
  - Semester completion
  - Student promotion system (considers retakes)
  - Graduation processing with Latin Honors
  - Report generation
  - Course status tracking
  - Student/Teacher/Room conflict detection
  - GWA-based unit limiting
  - Travel time calculations between buildings
  - Academic enrollment validation

#### Scheduling Engine

- **Schedule.java** - Intelligent scheduling with conflict detection (1200+ lines)
  - Optimal splice calculation
  - Resource availability checking
  - Mandatory break enforcement
  - Travel time integration
- **Scheduletab.java** - Schedule visualization with continuous blocks

#### File I/O

- **FileLoader.java** (and subclasses) - Load data from text files
- **FileSaver.java** (and subclasses) - Save data to text files
- **FilePathResolver.java** - Cross-platform path resolution

#### UI Components

- **ScoreTab.java** - Grade entry with auto-fill and validation
- **CourseTab.java** - Course enrollment with summer remedial support
- **Marksheettab.java** - Academic record viewing

### Compilation

```bash
cd ERS-group
javac -d bin -sourcepath src src/ers/group/*.java
```

**Output**: 52+ .class files in bin directory

---

## 🚀 Installation & Setup

### Prerequisites

- Java JDK 17 or higher
- Windows/macOS/Linux operating system

### Steps

1. **Clone or download the repository**

```bash
git clone https://github.com/A-Sabel/System---ERS.git
cd System---ERS
```

2. **Compile the project**

```bash
cd ERS-group
javac -d bin -sourcepath src src/ers/group/*.java
```

3. **Run the application**

```bash
java -cp bin ers.group.StudentCourseTab
```

### Directory Structure

```
System---ERS/
├── README.md
├── ERS-group/
│   ├── bin/ (compiled .class files)
│   └── src/
│       └── ers/
│           └── group/
│               ├── *.java (source files)
│               └── master files/
│                   ├── student.txt
│                   ├── enrollment.txt
│                   ├── marksheet.txt
│                   ├── courseSubject.txt
│                   ├── Schedule.txt
│                   ├── teachers.txt
│                   ├── rooms.txt
│                   ├── graduates.txt
│                   └── academic_calendar.txt
```

---

## 📖 Usage Guide

### Semester Completion Workflow

1. **Professors enter grades** in Score Tab
2. Grades saved to marksheet.txt with status ENROLLED
3. Admin opens **Admin Controls → Semester Management**
4. Click **"Complete Current Semester"**
5. System runs AcademicUtilities.processEndOfSemester()
6. All ENROLLED courses updated to PASSED/FAILED based on grades
7. Confirmation message shows success

### Student Promotion Workflow

1. Wait for both semesters (1st & 2nd) to complete
2. Admin opens **Admin Controls → Year Promotion**
3. Click **"View Promotion Candidates"** (optional)
4. Review list of eligible vs ineligible students
5. Click **"Promote 1st Year → 2nd Year"**
6. Confirm promotion dialog
7. System promotes all eligible students
8. student.txt updated: yearLevel changed from "1st Year" to "2nd Year"
9. Success message shows count of promoted students

### Graduation Processing Workflow

1. 2nd Year students complete both semesters
2. Admin opens **Admin Controls → Graduation**
3. Click **"View Graduation Candidates"** (optional)
4. Review graduation report with Latin Honors breakdown
5. Click **"Process Graduation"**
6. Confirm graduation dialog
7. System checks requirements (2nd Year, all courses passed, 60+ units, GWA ≤3.0)
8. Eligible students processed:
   - Record moved to graduates.txt with honors (NONE if any failed history)
   - Student status updated to "Graduate"
   - Removed from active student.txt
9. Success message shows graduated students list

### Summer Enrollment Workflow

1. After semester 2 completion, students with failed courses need summer remedial
2. Admin opens **Course Tab**
3. Select student and semester "Summer"
4. Click **"Load Available Courses"**
5. Only courses with current status FAILED/DROPPED/INC appear
6. Already-retaken and passed courses are filtered out
7. Enroll student in remedial courses (6-15 units max)
8. Generate schedule for summer intensive courses
9. After summer completion, failed courses become PASSED
10. Student becomes eligible for promotion/graduation

### Course Enrollment with Prerequisite Validation

1. Open Course Tab
2. Select student from table
3. Select semester (1st Semester, 2nd Semester, or Summer)
4. Click "Load Available Courses"
5. Select up to 5 courses from dropdowns
6. System validates:
   - Unit limits based on GWA
   - Prerequisites completion
   - Trio conflicts (student/teacher/room)
   - Summer special rules (only failed courses)
7. Click "Save" to enroll
8. System generates schedule with intelligent splicing

### Grade Entry and GWA Calculation

1. Open Score Tab
2. Search for student using Student ID
3. Select semester
4. Click "Auto-Fill Subjects" (loads enrolled courses)
5. Unused course slots are disabled/greyed out
6. Enter grades (1.00-5.00) for each course
7. System validates grade format
8. Click "Save" to persist to marksheet.txt
9. GWA automatically calculated and updated in student.txt

---

## 📁 File Structure

### student.txt Format

```
StudentID,Name,Age,DOB,YearLevel,StudentType,Address,Status,GWA,SubjectsEnrolled,Semester,Gender,Email,PhoneNumber,FathersName,MothersName,GuardiansPhone
```

### enrollment.txt Format

```
StudentID,CourseIDs,YearLevel,Semester,Status,SectionIDs,AcademicYear,DetailedStatuses
```

- **DetailedStatuses**: CS201:PASSED;CS202:FAILED (per-course status)

### marksheet.txt Format

```
MarksheetID,StudentID,Semester,YearLevel,Course1,Grade1,Course2,Grade2,...,Course5,Grade5,GWA
```

### Schedule.txt Format

```
ScheduleID,SectionID,Room,Day,StartTime,EndTime,TeacherName
```

### graduates.txt Format

```
StudentID,Name,GraduationDate,GWA,LatinHonors,AcademicYear,Degree
```

---

## ⚠️ Important Notes

### 2-Year Program Specific

1. **Summer semester is NOT required for promotion** - Only 1st and 2nd semesters must be completed
2. **2nd Year is the final year** - No promotion beyond 2nd Year
3. **Graduation processes only 2nd Year students** - System adapted for 2-year program
4. **COURSE RETAKES SUPPORTED** - Students can retake failed courses; latest status determines eligibility
5. **Latin Honors Disqualification** - Any history of FAILED/DROPPED/INC courses disqualifies from honors

### Summer Enrollment Special Rules

1. **3-15 Unit Limit** - Different from regular semesters (18-24 units)
2. **Failed Course Filtering** - Only courses with current FAILED/DROPPED/INC status
3. **Latest Status Check** - System tracks latest status across all enrollments
4. **Retaken Courses Excluded** - Courses already retaken and passed do not appear
5. **Intensive Daily Schedule** - Monday-Friday continuous scheduling

### Academic Progression Rules

1. **Promotion Eligibility** - Checks latest status of ALL required semester 1 & 2 courses
2. **Graduation Requirements** - All courses must have current status PASSED
3. **Unit Calculation** - Counts units from all courses with latest status PASSED
4. **GWA Calculation** - Based on all enrolled courses across all semesters
5. **Latin Honors** - Based on GWA but disqualified if any failed course history

### System Behavior

1. **File-based Persistence** - All changes immediately saved to text files
2. **Course Status Override** - Later enrollments override earlier course statuses
3. **Multiple Enrollments** - Student can have multiple enrollment records per year level
4. **Section Reuse** - Existing sections can be reused if schedules exist
5. **Schedule Generation** - Automatic schedule creation with conflict detection

---

## 🔄 Recent Updates

### Latest Enhancements (February 2026)

✅ **Course Retake Support** - Track latest status for each course across enrollments  
✅ **Summer Remedial Filtering** - Only show courses with current FAILED/DROPPED/INC status  
✅ **Promotion with Retakes** - Students eligible for promotion after retaking failed courses  
✅ **Graduation with Retakes** - Allow graduation but disqualify from Latin Honors  
✅ **Unit Calculation Fix** - Count units from all courses with latest status PASSED  
✅ **Latin Honors Rule** - Students with any failed history receive honors = NONE  
✅ **Requirements Panel Update** - Clarified "current status" language

---

## 📝 License & Usage

This project is developed for university requirements. Use at your own discretion.

**Important**: This is an educational project demonstrating software engineering principles for academic management systems.

---

## 👥 Contributors

Developed by:
Repository: [github.com/A-Sabel/System---ERS](https://github.com/A-Sabel/System---ERS)

---

## 📞 Support

For issues, questions, or contributions, please refer to the GitHub repository issues section.

---

**System-ERS** - Enrollment & Registration System for 2-Year Academic Programs
