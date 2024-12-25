package it.unito.annasabatelli.ecommerce.backend.repo;

import it.unito.annasabatelli.ecommerce.backend.model.Chart;
import org.springframework.data.repository.CrudRepository;

public interface ChartRepository extends CrudRepository<Chart, String> {
}
