package com.pao.laboratory03.exercise.service;

import com.pao.laboratory03.exercise.model.Student;
import com.pao.laboratory03.exercise.model.Subject;
import com.pao.laboratory03.exercise.exception.StudentNotFoundException;

import java.util.*;

public class StudentService {
    private static StudentService instance;
    private List<Student> students = new ArrayList<>();

    private StudentService() {}

    public static StudentService getInstance() {
        if (instance == null) instance = new StudentService();
        return instance;
    }

    public void addStudent(String name, int age) {
        for (Student s : students) {
            if (s.getName().equalsIgnoreCase(name)) {
                throw new RuntimeException("Studentul cu numele " + name + " exista deja.");
            }
        }
        students.add(new Student(name, age));
    }

    public Student findByName(String name) {
        for (int i =0; i< students.size();i++){
            if(Objects.equals(students.get(i).getName(), name))
                return students.get(i);
        }
        throw new StudentNotFoundException("Studentul " + name + "nu a fost gasit!");
    }

    public void addGrade(String studentName, Subject subject, double grade) {
        Student s = findByName(studentName);
        s.addGrade(subject, grade);
    }

    public void printAllStudents() {
        students.forEach(s -> System.out.println(s + " | Note: " + s.getGrades()));
    }

    public void printTopStudents() {
        students.sort((s1, s2) -> Double.compare(s2.getAverage(), s1.getAverage()));
        for(Student s : students)
            System.out.println(s);
    }

    public Map<Subject, Double> getAveragePerSubject() {
        Map<Subject, List<Double>> subjectGrades = new HashMap<>();

        for (Student s : students) {
            Map <Subject, Double> grades = s.getGrades();
            for(Map.Entry<Subject, Double> entry : grades.entrySet()){
                Subject subject = entry.getKey();
                Double grade = entry.getValue();

                if (!subjectGrades.containsKey(subject))
                    subjectGrades.put(subject, new ArrayList<>());

                subjectGrades.get(subject).add(grade);
            }
        }

        Map<Subject, Double> averages = new HashMap<>();

        for(Map.Entry<Subject, List<Double>> entry : subjectGrades.entrySet()){
            Subject sub = entry.getKey();
            List <Double> gradesList = entry.getValue();

            double sum = 0;
            for (Double g : gradesList){
                sum+=g;
            }
            double avg = sum/gradesList.size();
            averages.put(sub,avg);
        }
        return averages;
    }
}