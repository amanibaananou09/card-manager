package com.teknokote.cm.core.dao.impl;

import com.teknokote.cm.core.dao.CardGroupDao;
import com.teknokote.cm.core.dao.mappers.CardGroupMapper;
import com.teknokote.cm.core.model.CardGroup;
import com.teknokote.cm.core.model.Customer;
import com.teknokote.cm.core.model.User;
import com.teknokote.cm.core.repository.CardGroupRepository;
import com.teknokote.cm.dto.CardGroupDto;
import com.teknokote.core.dao.JpaActivatableGenericDao;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@Getter
@Setter
public class CardGroupDaoImpl extends JpaActivatableGenericDao<Long,User ,CardGroupDto, CardGroup> implements CardGroupDao
{
    @Autowired
    private CardGroupMapper mapper;
    @Autowired
    private CardGroupRepository repository;

    @Override
    protected CardGroup beforeCreate(CardGroup cardGroup, CardGroupDto dto) {
        cardGroup.setDateStatusChange(LocalDateTime.now());
        cardGroup.setCustomer(getEntityManager().getReference(Customer.class, dto.getCustomerId()));
        return super.beforeCreate(cardGroup, dto);
    }
    @Override
    protected CardGroup beforeUpdate(CardGroup cardGroup, CardGroupDto dto) {
        cardGroup.setDateStatusChange(LocalDateTime.now());
        cardGroup.setCustomer(getEntityManager().getReference(Customer.class, dto.getCustomerId()));
        return super.beforeUpdate(cardGroup, dto);
    }
   @Override
   public List<CardGroupDto> findAllByActif(boolean actif){
      return getRepository().findAllByActif(actif).stream().map(getMapper()::toDto).collect(Collectors.toList());
   }

    @Override
    public List<CardGroupDto> findAllByCustomer(Long customerId) {
        return getRepository().findAllByCustomerId(customerId).stream().map(getMapper()::toDto).collect(Collectors.toList());
    }

    @Override
    public CardGroupDto findByNameAndCustomerId(String name, Long customerId) {
        return getMapper().toDto(getRepository().findAllByNameAndCustomerId(name,customerId));
    }
}
