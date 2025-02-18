package com.interviewai.controller;

import com.interviewai.exception.ResourceNotFoundException;
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
    public ResponseEntity<List<InterviewQuestion>> getAllQuestions() {
        List<InterviewQuestion> questions = repository.findAll();
        return ResponseEntity.ok(questions);
    }

    // 2️⃣ 특정 국가 질문 조회
    @GetMapping("/{country}")
    public ResponseEntity<List<InterviewQuestion>> getQuestionsByCountry(@PathVariable String country) {
        List<InterviewQuestion> questions = repository.findByCountry(country);
        if (questions.isEmpty()) {
            throw new ResourceNotFoundException("해당 국가의 면접 질문이 없습니다: " + country);
        }
        return ResponseEntity.ok(questions);
    }

    // 3️⃣ 질문 추가
    @PostMapping
    public ResponseEntity<InterviewQuestion> addQuestion(@RequestBody InterviewQuestion question) {
        InterviewQuestion savedQuestion = repository.save(question);
        return ResponseEntity.ok(savedQuestion);
    }

    // 4️⃣ 질문 수정
    @PutMapping("/{id}")
    public ResponseEntity<InterviewQuestion> updateQuestion(@PathVariable Long id, @RequestBody InterviewQuestion updatedQuestion) {
        InterviewQuestion existingQuestion = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("해당 ID의 질문을 찾을 수 없습니다: " + id));

        existingQuestion.setCountry(updatedQuestion.getCountry());
        existingQuestion.setQuestion(updatedQuestion.getQuestion());
        InterviewQuestion savedQuestion = repository.save(existingQuestion);

        return ResponseEntity.ok(savedQuestion);
    }

    // 5️⃣ 질문 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long id) {
        InterviewQuestion question = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("해당 ID의 질문을 찾을 수 없습니다: " + id));

        repository.delete(question);
        return ResponseEntity.noContent().build(); // 204 No Content 응답
    }
}
