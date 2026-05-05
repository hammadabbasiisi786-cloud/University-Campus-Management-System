package com.campus.academicunit;

import com.campus.core.Academic_Unit;

import java.util.*;


class Department extends Academic_Unit {

    private String departmentName;
    private Professor headOfDepartment;

    ArrayList<Faculty> Faculties = new ArrayList<>();
    ArrayList<Course> courses = new ArrayList<>();



    public Department() {
        ID++;
        departmentId = ID;

    }

    public Department(String departmentName, Professor headOfDepartment) {
        ID++;
        departmentId = ID;
        setHeadOfDepartment(headOfDepartment);
        setDepartmentName(departmentName);

    }

    public void setDepartmentName(String departmentName) {
        if (departmentName == null || departmentName.isEmpty()) {
            System.out.println("Invalid Department Name");
        } else {
            this.departmentName = departmentName;
        }
    }

    public void setHeadOfDepartment(Professor headOfDepartment) {
        this.headOfDepartment = headOfDepartment;
    }
}
