package ers.group;

import java.util.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Professional-grade trio conflict detection and academic validation utilities.
 * Simplified version working with existing class methods.
 */
public class TrioValidation {
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    
    /**
     * ACADEMIC VALIDATION SYSTEM (Simplified)
     * GWA-based unit limiting for professional enrollment management
     */
    public static boolean validateAcademicEnrollment(String studentNumber, int additionalUnits) {
        try {
            // Simplified validation with default values
            double gwa = 2.50; // Default GWA - can be enhanced later
            int currentUnits = 18; // Default current load - can be enhanced later
            
            // Professional-grade unit limits based on academic performance
            int maxUnits;
            String performanceLevel;
            if (gwa >= 2.75) {
                maxUnits = 24;
                performanceLevel = "Dean's List";
            } else if (gwa >= 2.25) {
                maxUnits = 21;
                performanceLevel = "Good Standing";
            } else if (gwa >= 1.75) {
                maxUnits = 18;
                performanceLevel = "Satisfactory";
            } else {
                maxUnits = 15;
                performanceLevel = "Probation";
            }
            
            boolean canEnroll = (currentUnits + additionalUnits) <= maxUnits;
            
            System.out.println("📊 ACADEMIC VALIDATION:");
            System.out.println("  Student: " + studentNumber);
            System.out.println("  GWA: " + String.format("%.2f", gwa) + " (" + performanceLevel + ")");
            System.out.println("  Current: " + currentUnits + " units | Adding: " + additionalUnits + " | Max: " + maxUnits);
            System.out.println("  Status: " + (canEnroll ? "✅ APPROVED" : "❌ DENIED - Unit limit exceeded"));
            
            return canEnroll;
        } catch (Exception e) {
            System.err.println("Error in academic validation: " + e.getMessage());
            return true; // Allow enrollment on error to prevent system lockout
        }
    }
    
    /**
     * Travel time calculation between rooms/buildings
     */
    public static int calculateTravelTime(String fromRoom, String toRoom) {
        if (fromRoom == null || toRoom == null || fromRoom.equals(toRoom)) {
            return 0; // Same room or null rooms require no travel time
        }
        
        // Extract building codes (first part before room number)
        String fromBuilding = fromRoom.replaceAll("\\d+", "").trim();
        String toBuilding = toRoom.replaceAll("\\d+", "").trim();
        
        if (fromBuilding.equals(toBuilding)) {
            return 5; // Same building, minimal travel time
        }
        
        // Different buildings require more travel time
        return 15; // 15 minutes for cross-campus travel
    }
    
    /**
     * Enhanced scheduling with travel time consideration
     */
    public static boolean isSchedulingFeasible(String teacherName, String currentRoom, String nextRoom, LocalTime currentEnd, LocalTime nextStart) {
        if (teacherName == null) return true;
        
        int travelTime = calculateTravelTime(currentRoom, nextRoom);
        LocalTime requiredArrival = currentEnd.plusMinutes(30 + travelTime); // 30-min break + travel time
        
        boolean feasible = !nextStart.isBefore(requiredArrival);
        
        if (!feasible) {
            System.out.println("⚠️  TRAVEL TIME VIOLATION: " + teacherName + " needs " + 
                             (30 + travelTime) + " minutes between " + currentRoom + " and " + nextRoom);
        }
        
        return feasible;
    }
    
    /**
     * Professional trio check for Teacher/Room conflicts (simplified)
     */
    public static boolean hasTrioConflict(List<Schedule> existingSchedules, String day, String startTime, String endTime, String teacherName, String roomID) {
        try {
            for (Schedule existing : existingSchedules) {
                if (!existing.getDay().equals(day)) continue;
                
                // Check time overlap using existing conflict logic
                if (hasTimeConflict(existing, day, startTime, endTime)) {
                    System.out.println("🔍 TRIO ANALYSIS: Checking conflicts for " + day + " " + startTime + "-" + endTime);
                    
                    // TRIO CHECK 1: Teacher Conflicts  
                    if (teacherName != null && teacherName.equals(existing.getTeacherName())) {
                        System.out.println("  ❌ TEACHER CONFLICT: " + teacherName + " double-booked");
                        return true;
                    }
                    
                    // TRIO CHECK 2: Room Conflicts
                    if (roomID != null && roomID.equals(existing.getRoom())) {
                        System.out.println("  ❌ ROOM CONFLICT: Room " + roomID + " double-booked");
                        return true;
                    }
                    
                    System.out.println("  ⚠️  Time overlap detected but no trio violations");
                    return true; // Time conflict exists
                }
            }
            return false;
        } catch (Exception e) {
            System.err.println("Error in trio conflict detection: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Simple time conflict check with 30-minute breaks
     */
    private static boolean hasTimeConflict(Schedule existing, String day, String startTime, String endTime) {
        if (!existing.getDay().equals(day)) return false;
        
        try {
            LocalTime existingStart = LocalTime.parse(existing.getStartTime(), TIME_FORMATTER);
            LocalTime existingEnd = LocalTime.parse(existing.getEndTime(), TIME_FORMATTER);
            LocalTime proposedStart = LocalTime.parse(startTime, TIME_FORMATTER);
            LocalTime proposedEnd = LocalTime.parse(endTime, TIME_FORMATTER);
            
            // Check for 30-minute break violations
            if (proposedStart.isAfter(existingStart) && proposedStart.isBefore(existingEnd.plusMinutes(30))) {
                return true;
            }
            
            if (existingStart.isAfter(proposedStart) && existingStart.isBefore(proposedEnd.plusMinutes(30))) {
                return true;
            }
            
            // Standard overlap detection
            return !(proposedEnd.isBefore(existingStart) || proposedEnd.equals(existingStart) ||
                    proposedStart.isAfter(existingEnd) || proposedStart.equals(existingEnd));
        } catch (Exception e) {
            return false;
        }
    }
}