package client.repository;

import client.model.SecurityDefinition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SecurityDefinitionRepository extends JpaRepository<SecurityDefinition, Integer> {

    SecurityDefinition findByTicker(String ticker);

}
