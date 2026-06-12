package com.talenthub.job.api.controller;

import com.talenthub.job.api.dto.CreateSkillRequest;
import com.talenthub.job.api.dto.SkillResponse;
import com.talenthub.job.domain.model.Skill;
import com.talenthub.job.domain.repository.SkillRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(ApiPath.BASE + "/skills")
@RequiredArgsConstructor
public class SkillController {

    private final SkillRepository skillRepository;

    @PostMapping
    @Transactional
    public ResponseEntity<SkillResponse> create(@Valid @RequestBody CreateSkillRequest req) {
        if (skillRepository.existsByName(req.name())) {
            throw new IllegalArgumentException("Skill name đã tồn tại: " + req.name());
        }
        Skill saved = skillRepository.save(Skill.create(req.name(), req.type()));
        return ResponseEntity.status(HttpStatus.CREATED).body(SkillResponse.from(saved));
    }

    @GetMapping
    @Transactional(readOnly = true)
    public List<SkillResponse> list() {
        return skillRepository.findAll().stream().map(SkillResponse::from).toList();
    }
}
