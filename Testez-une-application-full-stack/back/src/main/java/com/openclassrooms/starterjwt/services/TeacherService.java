package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TeacherService {

  private final TeacherRepository teacherRepository;

  public TeacherService(TeacherRepository teacherRepository) {
    this.teacherRepository = teacherRepository;
  }

  public List<Teacher> findAll() {
    return this.teacherRepository.findAll();
  }

  public Teacher findById(Long id) {
    return this.teacherRepository.findById(id).orElse(null);
  }
}
