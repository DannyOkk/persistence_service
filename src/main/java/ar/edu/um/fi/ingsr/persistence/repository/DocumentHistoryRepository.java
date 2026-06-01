package ar.edu.um.fi.ingsr.persistence.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import ar.edu.um.fi.ingsr.persistence.domain.DocumentHistory;

public interface DocumentHistoryRepository extends JpaRepository<DocumentHistory, Integer> {
    
}
