package ar.edu.um.fi.ingsr.persistence.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ar.edu.um.fi.ingsr.persistence.domain.Conversation;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    List<Conversation> findByNotebookId(Long notebookId);
}
