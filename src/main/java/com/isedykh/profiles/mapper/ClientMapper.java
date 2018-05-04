package com.isedykh.profiles.mapper;

import com.isedykh.profiles.dao.entity.OrderEntity;
import com.isedykh.profiles.dao.entity.ClientEntity;
import com.isedykh.profiles.service.Client;
import com.isedykh.profiles.service.Order;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;
import org.mapstruct.Mapper;

import java.util.List;

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ClientMapper {

    Client personEntityToPerson(ClientEntity clientEntity);

    ClientEntity personToPersonEntity(Client client);

    List<Client> personEntitiesToPersons(List<ClientEntity> personEntities);

    List<ClientEntity> personsToPersonEntities(List<Client> clients);

    @Mapping(target = "client", ignore = true)
    Order orderEntityToOrder(OrderEntity orderEntity);
}
