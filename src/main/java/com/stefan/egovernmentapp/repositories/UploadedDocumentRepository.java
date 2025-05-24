package com.stefan.egovernmentapp.repositories;

import com.stefan.egovernmentapp.models.UploadedDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UploadedDocumentRepository extends JpaRepository<UploadedDocument, Integer> {
}
