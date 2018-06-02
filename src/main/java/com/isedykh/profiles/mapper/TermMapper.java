package com.isedykh.profiles.mapper;

import com.isedykh.profiles.dao.entity.TermEntity;
import com.isedykh.profiles.service.entity.Term;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TermMapper {

    Term termEntityToTerm(TermEntity termEntity);

    TermEntity termToTermEntity(Term term);

    List<Term> termEntitiesToTerms(List<TermEntity> termEntity);

    List<TermEntity> termsToTermEntities(List<Term> term);
}
