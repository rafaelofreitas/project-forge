package com.projectforge.template.infrastructure.mapper;

import com.projectforge.template.infrastructure.api.dto.UserInputDTO;
import com.projectforge.template.infrastructure.api.dto.UserOutputDTO;
import com.projectforge.template.domain.User;
import com.projectforge.template.infrastructure.persistence.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

  UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

  UserOutputDTO toOutput(User user);

  User toDomain(UserInputDTO userInputDTO);

  User toDomain(UserEntity userEntity);

  UserEntity toEntity(User user);
}
