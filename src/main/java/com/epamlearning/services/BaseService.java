package com.epamlearning.services;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BaseService<T> {

        T save(T t);
        T update(Long id, T t);
        T findById(Long id);
        List<T> findAll();
        void deleteById(Long id);

        T findByUsername(String username);
}
