package com.bensamir.starter.controller.service.impl;

import com.bensamir.starter.controller.service.CrudService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * JPA implementation of the CrudService interface.
 *
 * @param <T> The entity type
 * @param <ID> The entity ID type
 */
public abstract class JpaCrudService<T, ID> implements CrudService<T, ID> {

    /**
     * Gets the JPA repository.
     *
     * @return The JPA repository
     */
    protected abstract JpaRepository<T, ID> getRepository();

    @Override
    public T save(T entity) {
        return getRepository().save(entity);
    }

    @Override
    public Optional<T> findById(ID id) {
        return getRepository().findById(id);
    }

    @Override
    public List<T> findAll() {
        return getRepository().findAll();
    }

    @Override
    public Page<T> findAll(Pageable pageable) {
        return getRepository().findAll(pageable);
    }

    @Override
    public void deleteById(ID id) {
        getRepository().deleteById(id);
    }

    @Override
    public boolean existsById(ID id) {
        return getRepository().existsById(id);
    }
}