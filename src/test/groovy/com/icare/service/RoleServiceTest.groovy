package com.icare.service

import com.icare.dto.request.RoleRequest
import com.icare.dto.response.RoleResponse
import com.icare.entity.RoleEntity
import com.icare.exception.NotFoundException
import com.icare.repository.RoleRepository
import spock.lang.Specification

class RoleServiceTest extends Specification {
    private RoleService roleService
    private RoleRepository roleRepository

    void setup() {
        roleRepository = Mock()
        roleService = new RoleService(roleRepository)
    }

    def "addRole Successfully"() {
        given:
        def request = new RoleRequest(name: "Admin")

        when:
        roleService.addRole(request)

        then:
        1 * roleRepository.save(_)
    }

    def "getRole Successfully"() {
        given:
        def id = 1L
        def roleEntity = new RoleEntity(id: id, name: "Admin")
        def response = new RoleResponse(id: id, name: "Admin")

        when:
        def result = roleService.getRole(id)

        then:
        1 * roleRepository.findById(id) >> Optional.of(roleEntity)

        and:
        result.id == response.id
        result.name == response.name
    }

    def "getRole Throws NotFoundException"() {
        given:
        def id = 1L

        when:
        roleService.getRole(id)

        then:
        1 * roleRepository.findById(id) >> Optional.empty()

        and:
        def ex = thrown(NotFoundException)
        ex.message == "ROLE_NOT_FOUND"
    }

    def "getAllRoles"() {
        given:
        def roleEntities = [
                new RoleEntity(id: 1L, name: "Admin"),
                new RoleEntity(id: 2L, name: "User")
        ]

        when:
        def result = roleService.getAllRoles()

        then:
        1 * roleRepository.findAll() >> roleEntities

        and:
        result.size() == roleEntities.size()
        result.every { response ->
            roleEntities.any { entity ->
                entity.id == response.id &&
                        entity.name == response.name
            }
        }
    }

    def "updateRole Successfully"() {
        given:
        def id = 1L
        def request = new RoleRequest(name: "Moderator")
        def existingEntity = new RoleEntity(id: id, name: "Admin")
        def updatedEntity = new RoleEntity(id: id, name: "Moderator")
        def response = new RoleResponse(id: id, name: "Moderator")

        when:
        def result = roleService.updateRole(id, request)

        then:
        1 * roleRepository.findById(id) >> Optional.of(existingEntity)
        1 * roleRepository.save(_) >> updatedEntity

        and:
        result.id == response.id
        result.name == response.name
    }

    def "updateRole Throws NotFoundException"() {
        given:
        def id = 1L
        def request = new RoleRequest(name: "Moderator")

        when:
        roleService.updateRole(id, request)

        then:
        1 * roleRepository.findById(id) >> Optional.empty()
        0 * roleRepository.save(_)

        and:
        def ex = thrown(NotFoundException)
        ex.message == "ROLE_NOT_FOUND"
    }

    def "deleteRole"() {
        given:
        def id = 1L

        when:
        roleService.deleteRole(id)

        then:
        1 * roleRepository.deleteById(id)
    }
}
