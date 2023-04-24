package com.toyproject.complaints.domain.complaints.repository;

import com.toyproject.complaints.domain.complaints.entity.Complaints;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComplaintsRepository extends JpaRepository<Complaints, Long> {
    Complaints findOriginalEntityById(Long id);
    List<Complaints> findByComplaintId(String complaintId);
    Complaints findOriginalEntityByOriginalVoiceId(String originalVoiceID);
    List<Complaints> findByTitleContaining(String title);
    List<Complaints> findByContentContaining(String content);
    List<Complaints> findByTagContaining(String tag);
}
