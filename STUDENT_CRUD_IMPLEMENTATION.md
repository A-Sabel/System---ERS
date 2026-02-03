# Student CRUD Implementation

## Overview
Full CRUD (Create, Read, Update, Delete) operations with file persistence have been implemented in StudentCourseTab.java for the Student Management tab.

## Implemented Features

### 1. **Add New Student** (ST_AddNew button)
- **Functionality**: Creates a new student record from form input
- **Validation**: 
  - Requires Student ID and Name
  - Checks for duplicate Student IDs
- **Data Collection**:
  - Student ID (from ST_StudentID)
  - Student Name (from ST_StudentName)
  - Date of Birth (from ST_DateOfBirth JSpinner)
  - Age (auto-calculated from DOB)
  - Gender (from ST_Gender ComboBox)
  - Email (from ST_Email)
  - Phone Number (from ST_PhoneNumber)
  - Address (from ST_Address)
  - Father's Name (from ST_FathersName)
  - Mother's Name (from ST_MothersName)
  - Guardian's Phone (from ST_GuardiansPhoneNumber)
- **Default Values** (for fields not in form):
  - Year Level: "" (empty)
  - Student Type: "" (empty)
  - Subjects Enrolled: empty ArrayList
  - GWA: 0.0
- **Actions**:
  1. Validates required fields
  2. Checks for duplicate Student ID
  3. Creates new Student object
  4. Adds to students ArrayList
  5. Saves to file via StudentFileSaver
  6. Refreshes table display
  7. Clears form
  8. Shows success message

### 2. **Update Student** (ST_Update button)
- **Functionality**: Updates an existing student record
- **Validation**:
  - Requires a row to be selected in the table
  - Validates required fields (ID and Name)
  - Checks for duplicate Student ID if ID is changed
- **Data Preservation**:
  - Preserves Year Level, Student Type, Subjects Enrolled, and GWA from original record
  - Updates all form-visible fields with new values
- **Actions**:
  1. Gets selected student from table
  2. Validates selection and required fields
  3. Collects updated data from form
  4. Preserves fields not in the form
  5. Creates updated Student object
  6. Updates in students ArrayList
  7. Saves to file via StudentFileSaver
  8. Refreshes table display
  9. Clears form
  10. Shows success message

### 3. **Delete Student** (ST_Delete button) - Previously Implemented
- **Functionality**: Removes a student record
- **Actions**:
  1. Gets selected student from table
  2. Removes from table UI
  3. Removes from students ArrayList
  4. Saves to file via StudentFileSaver
  5. Shows success message

### 4. **Clear Form** (ST_Clear button)
- **Functionality**: Resets all form fields to empty/default values
- **Clears**:
  - All text fields
  - Gender combo box (to first option)
  - Date of Birth spinner (to current date)
  - Table selection

### 5. **Auto-Populate Form** (Table Row Selection)
- **Functionality**: When a table row is clicked, automatically fills form fields
- **Purpose**: Makes updating students easier - click row, edit fields, click Update
- **Populated Fields**:
  - Student ID
  - Student Name
  - Date of Birth
  - Gender
  - Email
  - Phone Number
  - Address
  - Father's Name
  - Mother's Name
  - Guardian's Phone

## File Persistence

All operations (Add, Update, Delete) automatically save changes to:
```
src/ers/group/master files/student.txt
```

The StudentFileSaver writes the complete student list in comma-separated format, maintaining compatibility with the existing file format.

## Form Fields

### Available in GUI:
1. ST_StudentID - JTextField
2. ST_StudentName - JTextField
3. ST_DateOfBirth - JSpinner (with date picker)
4. ST_Gender - JComboBox (Male/Female)
5. ST_Email - JTextField
6. ST_PhoneNumber - JTextField
7. ST_FathersName - JTextField
8. ST_MothersName - JTextField
9. ST_GuardiansPhoneNumber - JTextField
10. ST_Address - JTextField

### Not in Form (using defaults):
- Age - Auto-calculated from Date of Birth
- Year Level - Empty string (to be set elsewhere)
- Student Type - Empty string (to be set elsewhere)
- Subjects Enrolled - Empty ArrayList (to be managed via enrollment)
- GWA - 0.0 (to be calculated from scores)

## Usage Instructions

### Adding a New Student:
1. Fill in Student ID (required)
2. Fill in Student Name (required)
3. Set Date of Birth using the spinner
4. Select Gender from dropdown
5. Fill in other contact/family information
6. Click "Add New" button
7. Student is added to table and saved to file

### Updating a Student:
1. Click on a student row in the table (form auto-populates)
2. Modify any fields as needed
3. Click "Update" button
4. Changes are saved to table and file

### Deleting a Student:
1. Click on a student row in the table
2. Click "Delete" button
3. Student is removed from table and file

### Clearing the Form:
- Click "Clear" button to reset all fields

## Error Handling

All operations include try-catch blocks with user-friendly error messages:
- Validation errors (missing required fields)
- Duplicate Student ID errors
- File I/O errors
- Date parsing errors

## Technical Implementation

### Age Calculation:
```java
java.util.Calendar dobCal = java.util.Calendar.getInstance();
dobCal.setTime(dobDate);
java.util.Calendar today = java.util.Calendar.getInstance();
int age = today.get(java.util.Calendar.YEAR) - dobCal.get(java.util.Calendar.YEAR);
if (today.get(java.util.Calendar.DAY_OF_YEAR) < dobCal.get(java.util.Calendar.DAY_OF_YEAR)) {
    age--;
}
```

### Date Format:
- Display format: yyyy-MM-dd
- Stored format: yyyy-MM-dd (matching file format)

## Next Steps

The following features could be added in future updates:
1. Add form fields for Year Level, Student Type, and GWA
2. Implement Subjects Enrolled management (list/table of enrolled courses)
3. Add more robust validation (email format, phone number format)
4. Add search/filter functionality
5. Add export to PDF/Excel functionality
6. Implement undo/redo functionality
7. Add confirmation dialogs for destructive operations

## Testing Checklist

✅ Add new student with all fields
✅ Add new student with minimal fields (ID and Name only)
✅ Validate duplicate Student ID prevention
✅ Update existing student
✅ Update student with ID change
✅ Delete student
✅ Clear form
✅ Auto-populate form on row selection
✅ File persistence (check student.txt after operations)
✅ Age auto-calculation from DOB
✅ Date format handling

## Files Modified

- `StudentCourseTab.java`:
  - Added `ST_AddNewActionPerformed()` method
  - Added `ST_UpdateActionPerformed()` method
  - Added `ST_ClearActionPerformed()` method
  - Added `clearStudentForm()` helper method
  - Added `populateStudentFormFromTable()` helper method
  - Added action listeners for Add, Update, and Clear buttons
  - Added table selection listener for auto-populate feature
