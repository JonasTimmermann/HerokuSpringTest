package de.emp2020.Location;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface DistanceRepository extends CrudRepository<Distance, Integer>{
    
    

}