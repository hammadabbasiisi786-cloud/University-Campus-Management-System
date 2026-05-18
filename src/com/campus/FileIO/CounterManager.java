package com.campus.FileIO;

import java.io.Serializable;

public class CounterManager implements Serializable {

    // FIELDS
    private int studentCounter;
    private int courseCounter;
    private int classroomCounter;
    private int labCounter;
    private int teacherCounter;
    private int adminCounter;
    private int totalFacilityUsage;

    // CONSTRUCTORS
    public CounterManager() {}

    public CounterManager(int studentCounter, int courseCounter, int classroomCounter, int labCounter, int teacherCounter, int adminCounter, int totalFacilityUsage) {
        this.studentCounter    = studentCounter;
        this.teacherCounter    = teacherCounter;
        this.adminCounter      = adminCounter;
        this.labCounter        = labCounter;
        this.courseCounter     = courseCounter;
        this.classroomCounter  = classroomCounter;
        this.totalFacilityUsage = totalFacilityUsage;
    }

    // GETTERS
    public int getStudentCounter() { return studentCounter; }
    public int getTeacherCounter() { return teacherCounter; }
    public int getAdminCounter() { return adminCounter; }
    public int getCourseCounter() { return courseCounter; }
    public int getClassroomCounter() { return classroomCounter; }
    public int getLabCounter() { return labCounter; }
    public int getTotalFacilityUsage() { return totalFacilityUsage; }
}