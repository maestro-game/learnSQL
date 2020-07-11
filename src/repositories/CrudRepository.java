package repositories;

import java.util.List;

public interface CrudRepository<T> {
    List<T> findAll();
    T findById(long id);
    void save(T entity);
    void update(T entity);
}
