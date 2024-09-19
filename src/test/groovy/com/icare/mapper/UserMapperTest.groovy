package com.icare.mapper

import com.icare.dto.request.UserUpdateRequest
import com.icare.entity.UserEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import spock.lang.Specification
import com.icare.dto.request.UserRegisterRequest
import io.github.benas.randombeans.EnhancedRandomBuilder

class UserMapperTest extends Specification {
    private def random

    void setup() {
        random = EnhancedRandomBuilder.aNewEnhancedRandomBuilder().build()
    }

    def "RegisterRequestToEntity"() {
        given:
        UserRegisterRequest userRequest = random.nextObject(UserRegisterRequest)

        when:
        def userEntity = UserMapper.INSTANCE.registerRequestToEntity(userRequest)

        then:
        userEntity.email == userRequest.email
        userEntity.password == userRequest.password
        userEntity.pin == userRequest.pin
        userEntity.name == userRequest.name
        userEntity.surname == userRequest.surname
        userEntity.birthDay == userRequest.birthDay
        userEntity.phoneNumber == userRequest.phoneNumber
    }

    def "EntityToResponse"() {
        given:
        UserEntity userEntity = random.nextObject(UserEntity)

        when:
        def userResponse = UserMapper.INSTANCE.entityToResponse(userEntity)

        then:
        userResponse.id == userEntity.id
        userResponse.name == userEntity.name
        userResponse.surname == userEntity.surname
        userResponse.birthDay == userEntity.birthDay
        userResponse.email == userEntity.email
        userResponse.phoneNumber == userEntity.phoneNumber
        userResponse.photoUrl == userEntity.photoUrl
    }

    def "EntitiesToResponses"() {
        given:
        List<UserEntity> userEntities = [
                random.nextObject(UserEntity),
                random.nextObject(UserEntity),
                random.nextObject(UserEntity)
        ]
        def pageable = PageRequest.of(0, 10)
        def userEntitiesPage = new PageImpl<>(userEntities, pageable, userEntities.size())

        when:
        def userResponses = UserMapper.INSTANCE.entitiesToResponses(userEntitiesPage as Page<UserEntity>)

        then:
        userResponses.size() == userEntities.size()
        userResponses.every { response ->
            userEntities.any { entity ->
                entity.id == response.id &&
                        entity.name == response.name &&
                        entity.surname == response.surname &&
                        entity.birthDay == response.birthDay &&
                        entity.email == response.email &&
                        entity.phoneNumber == response.phoneNumber &&
                        entity.photoUrl == response.photoUrl
            }
        }
    }

    def "MapRequestToEntity"() {
        given:
        UserUpdateRequest request = random.nextObject(UserUpdateRequest)
        UserEntity entity = new UserEntity()

        when:
        UserMapper.INSTANCE.mapRequestToEntity(entity, request)

        then:
        entity.email == request.email
        entity.phoneNumber == request.phoneNumber
    }
}
