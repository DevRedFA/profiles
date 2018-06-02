package com.isedykh.profiles.service;

import com.isedykh.profiles.service.entity.Term;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TermService extends CrudService<Term> {

    List<Term> findAll();

    Page<Term> findAll(Pageable pageable);

    Term save(Term term);

    void delete(Term term);

    Term findById(long id);
}
