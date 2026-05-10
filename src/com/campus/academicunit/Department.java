package com.campus.academicunit;

import com.campus.Interfaces.Reportable;

import com.campus.core.Academic_unit;

import java.util.*;


class Department extends Academic_unit implements Reportable {

    ArrayList<Faculty> Faculties = new ArrayList<>();
    ArrayList<Course> courses = new ArrayList<>();
    private String departmentName;
    private Professor headOfDepartment;


    public Department() {

    }

    public Department(String departmentName, Professor headOfDepartment) {
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

    public String getDepartmentName() {return departmentName;}

    public Professor getHeadOfDepartment() {return headOfDepartment;}
}
