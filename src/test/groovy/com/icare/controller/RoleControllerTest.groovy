package com.icare.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.icare.dto.request.RoleRequest
import com.icare.dto.response.RoleResponse
import com.icare.exception.NotFoundException
import com.icare.service.RoleService
import io.github.benas.randombeans.EnhancedRandomBuilder
import org.springframework.http.HttpStatus
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

class RoleControllerTest extends Specification {
    private def random
    private def mapper
    private def mockMvc
    private def roleController
    private RoleService roleService

    void setup() {
        roleService = Mock()
        roleController = new RoleController(roleService)
        random = EnhancedRandomBuilder.aNewEnhancedRandomBuilder().build()
        mapper = new ObjectMapper().registerModule(new JavaTimeModule())
        mockMvc = MockMvcBuilders.standaloneSetup(roleController).setControllerAdvice(new ErrorHandler()).build()
    }

    def "AddRole 200"() {
        given:
        def url = "/roles"
        def roleRequestJson = '''
            {
                "name": "Admin"
            }
        '''
        def roleRequest = new ObjectMapper().readValue(roleRequestJson, RoleRequest)

        when:
        def response = mockMvc.perform(post(url)
                .contentType(APPLICATION_JSON)
                .content(roleRequestJson)
        ).andReturn().response

        then:
        1 * roleService.addRole(roleRequest)

        and:
        response.status == HttpStatus.CREATED.value()
    }

    def "GetRole 200"() {
        given:
        def id = 1L
        def url = "/roles/$id"
        def roleResponse = RoleResponse
                .builder()
                .id(id)
                .name("Admin")
                .build()

        when:
        def result = mockMvc.perform(get(url)
                .contentType(APPLICATION_JSON)
        ).andReturn().response

        then:
        1 * roleService.getRole(id) >> roleResponse

        and:
        result.status == HttpStatus.OK.value()
    }

    def "GetRole 404"() {
        given:
        def id = 1L
        def url = "/roles/$id"
        def response = '''
            {
                "timestamp": "2024-September-19 17:50:29",
                "message": "ROLE_NOT_FOUND",
                "code": "NOT_FOUND",
                "status": 404
            }
        '''

        when:
        def result = mockMvc.perform(get(url)
                .contentType(APPLICATION_JSON)
        ).andReturn().response

        then:
        1 * roleService.getRole(id) >> {
            throw new NotFoundException("ROLE_NOT_FOUND")
        }

        and:
        result.status == HttpStatus.NOT_FOUND.value()
        JSONAssert.assertEquals(result.contentAsString, response, false)
    }

    def "GetAllRoles 200"() {
        given:
        def url = "/roles"
        def roleResponses = [
                random.nextObject(RoleResponse),
                random.nextObject(RoleResponse),
                random.nextObject(RoleResponse)
        ]

        when:
        def response = mockMvc.perform(get(url)
                .contentType(APPLICATION_JSON)
        ).andReturn().response

        then:
        1 * roleService.getAllRoles() >> roleResponses

        and:
        response.status == HttpStatus.OK.value()
    }

    def "UpdateRole 200"() {
        given:
        def id = 1L
        def url = "/roles/$id"
        def roleRequestJson = '''
            {
                "name": "UpdatedRole"
            }
        '''
        def roleRequest = new ObjectMapper().readValue(roleRequestJson, RoleRequest)
        def roleResponse = RoleResponse
                .builder()
                .id(id)
                .name("UpdatedRole")
                .build()

        when:
        def result = mockMvc.perform(put(url)
                .contentType(APPLICATION_JSON)
                .content(roleRequestJson)
        ).andReturn().response

        then:
        1 * roleService.updateRole(id, roleRequest) >> roleResponse

        and:
        result.status == HttpStatus.OK.value()
    }

    def "DeleteRole 204"() {
        given:
        def id = 1L
        def url = "/roles/$id"

        when:
        def result = mockMvc.perform(delete(url)
                .contentType(APPLICATION_JSON)
        ).andReturn().response

        then:
        1 * roleService.deleteRole(id)

        and:
        result.status == HttpStatus.NO_CONTENT.value()
    }
}