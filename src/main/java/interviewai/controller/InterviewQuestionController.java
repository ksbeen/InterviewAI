package com.interviewai.controller;

import com.interviewai.model.InterviewQuestion;
import com.interviewai.repository.InterviewQuestionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/questions")
public class InterviewQuestionController {

    private final InterviewQuestionRepository repository;

    public InterviewQuestionController(InterviewQuestionRepository repository) {
        this.repository = repository;
    }

    // 1️⃣ 모든 질문 조회
    @GetMapping
    public List<InterviewQuestion> getAllQuestions() {
        return repository.findAll();
    }

    // 2️⃣ 특정 국가 질문 조회
    @GetMapping("/{country}")
    public List<InterviewQuestion> getQuestionsByCountry(@PathVariable String country) {
        return repository.findByCountry(country);
    }

    // 3️⃣ 질문 추가
    @PostMapping
    public ResponseEntity<InterviewQuestion> addQuestion(@RequestBody InterviewQuestion question) {
        InterviewQuestion savedQuestion = repository.save(question);
        return ResponseEntity.ok(savedQuestion);
    }

    // 4️⃣ 질문 수정
    @PutMapping("/{id}")
    public ResponseEntity<InterviewQuestion> updateQuestion(
            @PathVariable Long id,
            @RequestBody InterviewQuestion updatedQuestion) {
        return repository.findById(id)
                .map(question -> {
                    question.setCountry(updatedQuestion.getCountry());
                    question.setQuestion(updatedQuestion.getQuestion());
                    InterviewQuestion savedQuestion = repository.save(question);
                    return ResponseEntity.ok(savedQuestion);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // 5️⃣ 질문 삭제 API 
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteQuestion(@PathVariable Long id) {
        return repository.findById(id)
                .map(question -> {
                    repository.delete(question);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}