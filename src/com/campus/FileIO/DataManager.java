package com.campus.FileIO;

import com.campus.*;
import com.campus.Person.*;
import com.campus.academicunit.*;
import com.campus.core.*;
import com.campus.facility.*;
import com.campus.Serviceunit.*;

import java.io.*;
import java.util.Timer;
import java.util.TimerTask;

public class DataManager {

    // FILEPATHS
    private static final String DATA_FOLDER = "data/";
    private static final String STUDENTS_FILE = DATA_FOLDER + "students.dat";
    private static final String TEACHERS_FILE = DATA_FOLDER + "teachers.dat";
    private static final String COURSES_FILE = DATA_FOLDER + "courses.dat";
    private static final String DEPARTMENTS_FILE = DATA_FOLDER + "departments.dat";
    private static final String CLASSROOMS_FILE = DATA_FOLDER + "classrooms.dat";
    private static final String LABS_FILE = DATA_FOLDER + "labs.dat";
    private static final String BOOKS_FILE = DATA_FOLDER + "books.dat";
    private static final String HOSTELS_FILE = DATA_FOLDER + "hostels.dat";
    private static final String CAFETERIAS_FILE = DATA_FOLDER + "cafeterias.dat";
    private static final String LIBRARIES_FILE = DATA_FOLDER + "libraries.dat";
    private static final String HEALTH_FILE = DATA_FOLDER + "healthcenters.dat";
    private static final String SECURITY_FILE = DATA_FOLDER + "securityservices.dat";
    private static final String TRANSPORT_FILE = DATA_FOLDER + "transportservices.dat";
    private static final String ZONES_FILE = DATA_FOLDER + "campuszones.dat";
    private static final String LOGIN_FILE = DATA_FOLDER + "loginmanager.dat";
    private static final String COUNTERS_FILE = DATA_FOLDER + "counters.dat";

    // FIELDS

    private CampusRepository<Student> repoStudents = new CampusRepository<>();
    private CampusRepository<Teacher> repoTeachers = new CampusRepository<>();
    private CampusRepository<Course> repoCourses = new CampusRepository<>();
    private CampusRepository<Department> repoDepartments = new CampusRepository<>();
    private CampusRepository<Classroom> repoClassrooms = new CampusRepository<>();
    private CampusRepository<Lab> repoLabs = new CampusRepository<>();
    private CampusRepository<Book> repoBooks = new CampusRepository<>();
    private CampusRepository<Hostel> repoHostels = new CampusRepository<>();
    private CampusRepository<Cafeteria> repoCafeterias = new CampusRepository<>();
    private CampusRepository<Library> repoLibraries = new CampusRepository<>();
    private CampusRepository<HealthCenter> repoHealthCenters = new CampusRepository<>();
    private CampusRepository<SecurityService> repoSecurityServices = new CampusRepository<>();
    private CampusRepository<TransportService> repoTransportServices = new CampusRepository<>();
    private CampusRepository<CampusZone> repoZones = new CampusRepository<>();
    private LoginManager loginManager = new LoginManager();
    private Timer autoSaveTimer;

    // CONSTRUCTORS
    public DataManager() {
        // Ensure the data folder exists before any save/load operations
        new File(DATA_FOLDER).mkdirs();
    }

    // GETTERS
    public CampusRepository<Student> getRepoStudents() {
        return repoStudents;
    }

    public CampusRepository<Teacher> getRepoTeachers() {
        return repoTeachers;
    }

    public CampusRepository<Course> getRepoCourses() {
        return repoCourses;
    }

    public CampusRepository<Department> getRepoDepartments() {
        return repoDepartments;
    }

    public CampusRepository<Classroom> getRepoClassrooms() {
        return repoClassrooms;
    }

    public CampusRepository<Lab> getRepoLabs() {
        return repoLabs;
    }

    public CampusRepository<Book> getRepoBooks() {
        return repoBooks;
    }

    public CampusRepository<Hostel> getRepoHostels() {
        return repoHostels;
    }

    public CampusRepository<Cafeteria> getRepoCafeterias() {
        return repoCafeterias;
    }

    public CampusRepository<Library> getRepoLibraries() {
        return repoLibraries;
    }

    public CampusRepository<HealthCenter> getRepoHealthCenters() {
        return repoHealthCenters;
    }

    public CampusRepository<SecurityService> getRepoSecurityServices() {
        return repoSecurityServices;
    }

    public CampusRepository<TransportService> getRepoTransportServices() {
        return repoTransportServices;
    }

    public CampusRepository<CampusZone> getRepoZones() {
        return repoZones;
    }

    public LoginManager getLoginManager() {
        return loginManager;
    }

    // OTHER METHODS

    public void saveAll() {
        System.out.println("\n--- Saving all data ---");
        save(repoStudents, STUDENTS_FILE);
        save(repoTeachers, TEACHERS_FILE);
        save(repoCourses, COURSES_FILE);
        save(repoDepartments, DEPARTMENTS_FILE);
        save(repoClassrooms, CLASSROOMS_FILE);
        save(repoLabs, LABS_FILE);
        save(repoBooks, BOOKS_FILE);
        save(repoHostels, HOSTELS_FILE);
        save(repoCafeterias, CAFETERIAS_FILE);
        save(repoLibraries, LIBRARIES_FILE);
        save(repoHealthCenters, HEALTH_FILE);
        save(repoSecurityServices, SECURITY_FILE);
        save(repoTransportServices, TRANSPORT_FILE);
        save(repoZones, ZONES_FILE);
        save(loginManager, LOGIN_FILE);
        saveCounters();
        System.out.println("--- All data saved successfully ---\n");
    }

    @SuppressWarnings("unchecked")
    public void loadAll() {
        System.out.println("\n--- Loading all data ---");

        CampusRepository<Student> loadedStudents = (CampusRepository<Student>) load(STUDENTS_FILE);
        if (loadedStudents != null)
            repoStudents = loadedStudents;

        CampusRepository<Teacher> loadedTeachers = (CampusRepository<Teacher>) load(TEACHERS_FILE);
        if (loadedTeachers != null)
            repoTeachers = loadedTeachers;

        CampusRepository<Course> loadedCourses = (CampusRepository<Course>) load(COURSES_FILE);
        if (loadedCourses != null)
            repoCourses = loadedCourses;

        CampusRepository<Department> loadedDepts = (CampusRepository<Department>) load(DEPARTMENTS_FILE);
        if (loadedDepts != null)
            repoDepartments = loadedDepts;

        CampusRepository<Classroom> loadedClassrooms = (CampusRepository<Classroom>) load(CLASSROOMS_FILE);
        if (loadedClassrooms != null)
            repoClassrooms = loadedClassrooms;

        CampusRepository<Lab> loadedLabs = (CampusRepository<Lab>) load(LABS_FILE);
        if (loadedLabs != null)
            repoLabs = loadedLabs;

        CampusRepository<Book> loadedBooks = (CampusRepository<Book>) load(BOOKS_FILE);
        if (loadedBooks != null)
            repoBooks = loadedBooks;

        CampusRepository<Hostel> loadedHostels = (CampusRepository<Hostel>) load(HOSTELS_FILE);
        if (loadedHostels != null)
            repoHostels = loadedHostels;

        CampusRepository<Cafeteria> loadedCafeterias = (CampusRepository<Cafeteria>) load(CAFETERIAS_FILE);
        if (loadedCafeterias != null)
            repoCafeterias = loadedCafeterias;

        CampusRepository<Library> loadedLibraries = (CampusRepository<Library>) load(LIBRARIES_FILE);
        if (loadedLibraries != null)
            repoLibraries = loadedLibraries;

        CampusRepository<HealthCenter> loadedHealth = (CampusRepository<HealthCenter>) load(HEALTH_FILE);
        if (loadedHealth != null)
            repoHealthCenters = loadedHealth;

        CampusRepository<SecurityService> loadedSecurity = (CampusRepository<SecurityService>) load(SECURITY_FILE);
        if (loadedSecurity != null)
            repoSecurityServices = loadedSecurity;

        CampusRepository<TransportService> loadedTransport = (CampusRepository<TransportService>) load(TRANSPORT_FILE);
        if (loadedTransport != null)
            repoTransportServices = loadedTransport;

        CampusRepository<CampusZone> loadedZones = (CampusRepository<CampusZone>) load(ZONES_FILE);
        if (loadedZones != null)
            repoZones = loadedZones;

        LoginManager loadedLogin = (LoginManager) load(LOGIN_FILE);
        if (loadedLogin != null)
            loginManager = loadedLogin;

        loadCounters();

        System.out.println("--- All data loaded successfully ---\n");
    }

    public void startAutoSave() {
        autoSaveTimer = new Timer(true); // daemon=true so it doesn't block app exit
        autoSaveTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                System.out.println("[Auto-Save] Saving data at " + new java.util.Date());
                saveAll();
            }
        }, 5 * 60 * 1000, 5 * 60 * 1000); // 5 min delay, 5 min interval
        System.out.println("[Auto-Save] Timer started — saves every 5 minutes.");
    }

    public void stopAutoSave() {
        if (autoSaveTimer != null) {
            autoSaveTimer.cancel();
            System.out.println("[Auto-Save] Timer stopped.");
        }
    }

    private void save(Object data, String filepath) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filepath))) {
            oos.writeObject(data);
            System.out.println("  Saved : " + filepath);
        } catch (IOException e) {
            System.out.println("  Save FAILED : " + filepath + " → " + e.getMessage());
        }
    }

    private Object load(String filepath) {
        File file = new File(filepath);

        if (!file.exists()) {
            System.out.println("  No file  : " + filepath + " → starting fresh");
            return null;
        }

        if (file.length() == 0) {
            System.out.println("  Empty    : " + filepath + " → starting fresh");
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filepath))) {
            Object data = ois.readObject();
            System.out.println("  Loaded   : " + filepath);
            return data;
        } catch (IOException e) {
            System.out.println("  Corrupted: " + filepath + " → starting fresh | " + e.getMessage());
            return null;
        } catch (ClassNotFoundException e) {
            System.out.println("  Mismatch : " + filepath + " → class changed, starting fresh | " + e.getMessage());
            return null;
        }
    }

    private void saveCounters() {
        CounterManager manager = new CounterManager(Student.getStudentCounter(), Course.getTotalCourses(),
                Classroom.getIdCounter(), Lab.getIdCounter(), Teacher.getTeacherCounter(), Admin.getAdminCounter(),
                Department.getIdCounter(), Facility.getTotalFacilityUsage());
        save(manager, COUNTERS_FILE);
    }

    private void loadCounters() {
        CounterManager manager = (CounterManager) load(COUNTERS_FILE);
        if (manager != null) {
            Student.setStudentCounter(manager.getStudentCounter());
            Course.setIdCounter(manager.getCourseCounter());
            Classroom.setIdCounter(manager.getClassroomCounter());
            Lab.setIdCounter(manager.getLabCounter());
            Teacher.setTeacherCounter(manager.getTeacherCounter());
            Admin.setAdminCounter(manager.getAdminCounter());
            Department.setIdCounter(manager.getDepartmentCounter());
            Facility.setFacilityCounter(manager.getTotalFacilityUsage());
            System.out.println("  Counters restored → Student:" + manager.getStudentCounter() + " Teacher:"
                    + manager.getTeacherCounter() + " Admin:" + manager.getAdminCounter() + " Course:"
                    + manager.getCourseCounter() + " Classroom:" + manager.getClassroomCounter() + " Lab:"
                    + manager.getLabCounter() + " Dept:" + manager.getDepartmentCounter()
                    + " Facility:" + manager.getTotalFacilityUsage());
        }
    }

}