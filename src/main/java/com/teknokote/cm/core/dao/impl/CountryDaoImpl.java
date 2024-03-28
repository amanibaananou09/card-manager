package com.teknokote.cm.core.dao.impl;

import com.teknokote.cm.core.dao.CountryDao;
import com.teknokote.cm.core.dao.mappers.CountryMapper;
import com.teknokote.cm.core.model.Country;
import com.teknokote.cm.core.repository.CountryRepository;
import com.teknokote.cm.dto.CountryDto;
import com.teknokote.core.dao.JpaGenericDao;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
@Getter
@Setter
public class CountryDaoImpl extends JpaGenericDao<Long,CountryDto, Country> implements CountryDao
{
    @Autowired
    private CountryMapper mapper;
    @Autowired
    private CountryRepository repository;
}
