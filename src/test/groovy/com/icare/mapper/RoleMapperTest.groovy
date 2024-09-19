package com.icare.mapper

import com.icare.dto.request.RoleRequest
import com.icare.entity.RoleEntity
import io.github.benas.randombeans.EnhancedRandomBuilder
import spock.lang.Specification

class RoleMapperTest extends Specification {
    private def random

    void setup() {
        random = EnhancedRandomBuilder.aNewEnhancedRandomBuilder().build()
    }

    def "RequestToEntity"() {
        given:
        RoleRequest roleRequest = random.nextObject(RoleRequest)

        when:
        def roleEntity = RoleMapper.INSTANCE.requestToEntity(roleRequest)

        then:
        roleEntity.name == roleRequest.name
    }

    def "EntityToResponse"() {
        given:
        RoleEntity roleEntity = random.nextObject(RoleEntity)

        when:
        def roleResponse = RoleMapper.INSTANCE.entityToResponse(roleEntity)

        then:
        roleResponse.id == roleEntity.id
        roleResponse.name == roleEntity.name
    }

    def "EntitiesToResponses"() {
        given:
        List<RoleEntity> roleEntities = [
                random.nextObject(RoleEntity),
                random.nextObject(RoleEntity),
                random.nextObject(RoleEntity)
        ]

        when:
        def roleResponses = RoleMapper.INSTANCE.entitiesToResponses(roleEntities)

        then:
        roleResponses.size() == roleEntities.size()
        roleResponses.every { response ->
            roleEntities.any { entity ->
                entity.id == response.id &&
                        entity.name == response.name
            }
        }
    }

    def "MapRequestToEntity"() {
        given:
        RoleRequest roleRequest = random.nextObject(RoleRequest)
        RoleEntity roleEntity = new RoleEntity()

        when:
        RoleMapper.INSTANCE.mapRequestToEntity(roleEntity, roleRequest)

        then:
        roleEntity.name == roleRequest.name
    }
}

