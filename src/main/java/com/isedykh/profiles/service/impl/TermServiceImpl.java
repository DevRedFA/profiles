package com.isedykh.profiles.service.impl;

import com.isedykh.profiles.dao.entity.TermEntity;
import com.isedykh.profiles.dao.repository.TermEntityRepository;
import com.isedykh.profiles.mapper.TermMapper;
import com.isedykh.profiles.service.TermService;
import com.isedykh.profiles.service.entity.Term;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TermServiceImpl implements TermService {

    private final TermEntityRepository termEntityRepository;

    private final TermMapper termMapper;

    @Override
    public List<Term> findAll() {
        return termMapper.termEntitiesToTerms(termEntityRepository.findAll());
    }

    @Override
    public Page<Term> findAll(Pageable pageable) {
        Page<TermEntity> all = termEntityRepository.findAll(pageable);
        List<Term> terms = termMapper.termEntitiesToTerms(all.getContent());
        return new PageImpl<>(terms, all.getPageable(), all.getTotalElements());
    }

    @Override
    public Term save(Term term) {
        TermEntity save = termEntityRepository.save(termMapper.termToTermEntity(term));
        return termMapper.termEntityToTerm(save);
    }

    @Override
    public void delete(Term term) {
        termEntityRepository.delete(termMapper.termToTermEntity(term));
    }

    @Override
    public void delete(long id) {
        termEntityRepository.deleteById(id);
    }

    @Override
    public Term findById(long id) {
        return termMapper.termEntityToTerm(termEntityRepository.getOne(id));
    }
}
