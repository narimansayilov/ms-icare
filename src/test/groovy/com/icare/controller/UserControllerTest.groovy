package com.icare.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.icare.dto.criteria.UserCriteriaRequest
import com.icare.dto.request.UserUpdateRequest
import com.icare.dto.response.UserResponse
import com.icare.service.UserService
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

class UserControllerTest extends Specification {
    private ObjectMapper mapper
    private MockMvc mockMvc
    private UserService userService
    private UserController userController

    void setup() {
        userService = Mock()
        userController = new UserController(userService)
        mapper = new ObjectMapper().registerModule(new JavaTimeModule())
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build()
    }

    def "GetMyProfile 200"() {
        given:
        def url = "/users/my"
        def userResponse = new UserResponse(id: 1L, name: "John", surname: "Doe", email: "john.doe@example.com")

        when:
        def response = mockMvc.perform(get(url)
                .contentType(APPLICATION_JSON)
        ).andReturn().response

        then:
        1 * userService.getMyProfile() >> userResponse

        and:
        response.status == HttpStatus.OK.value()
        response.contentAsString == mapper.writeValueAsString(userResponse)
    }

    def "GetUser 200"() {
        given:
        def id = 1L
        def url = "/users/$id"
        def userResponse = new UserResponse(id: id, name: "John", surname: "Doe", email: "john.doe@example.com")

        when:
        def response = mockMvc.perform(get(url)
                .contentType(APPLICATION_JSON)
        ).andReturn().response

        then:
        1 * userService.getUser(id) >> userResponse

        and:
        response.status == HttpStatus.OK.value()
        response.contentAsString == mapper.writeValueAsString(userResponse)
    }

    def "GetAllUsers 200"() {
        given:
        def url = "/users/all"
        def pageable = new org.springframework.data.domain.PageRequest(0, 10)
        def userResponses = [
                new UserResponse(id: 1L, name: "John", surname: "Doe", email: "john.doe@example.com"),
                new UserResponse(id: 2L, name: "Jane", surname: "Doe", email: "jane.doe@example.com")
        ]
        def criteriaRequest = new UserCriteriaRequest()

        when:
        def response = mockMvc.perform(get(url)
                .param("page", "0")
                .param("size", "10")
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(criteriaRequest))
        ).andReturn().response

        then:
        1 * userService.getAllUsers(_, _) >> userResponses

        and:
        response.status == HttpStatus.OK.value()
        response.contentAsString == mapper.writeValueAsString(userResponses)
    }

    def "UpdateUser 200"() {
        given:
        def url = "/users/update"
        def userUpdateRequest = new UserUpdateRequest(email: "updated.email@example.com", phoneNumber: "123456789")
        def image = new MockMultipartFile("image", "test.jpg", "image/jpeg", new byte[0])
        def userResponse = new UserResponse(id: 1L, name: "Updated Name", surname: "Doe", email: "updated.email@example.com")

        when:
        def result = mockMvc.perform(multipart(url)
                .file(image)
                .param("request", mapper.writeValueAsString(userUpdateRequest))
                .contentType(APPLICATION_JSON)
        ).andReturn().response

        then:
        1 * userService.update(userUpdateRequest, _) >> userResponse

        and:
        result.status == HttpStatus.OK.value()
        result.contentAsString == mapper.writeValueAsString(userResponse)
    }

    def "ActivateUser 200"() {
        given:
        def id = 1L
        def url = "/users/$id/activate"

        when:
        def result = mockMvc.perform(patch(url)
                .contentType(APPLICATION_JSON)
        ).andReturn().response

        then:
        1 * userService.activateUser(id)

        and:
        result.status == HttpStatus.OK.value()
    }

    def "DeactivateUser 200"() {
        given:
        def id = 1L
        def url = "/users/$id/deactivate"

        when:
        def result = mockMvc.perform(patch(url)
                .contentType(APPLICATION_JSON)
        ).andReturn().response

        then:
        1 * userService.deactivateUser(id)

        and:
        result.status == HttpStatus.OK.value()
    }

    def "SetRole 200"() {
        given:
        def userId = 1L
        def roleId = 2L
        def url = "/users/set-role"

        when:
        def result = mockMvc.perform(patch(url)
                .param("user", userId.toString())
                .param("role", roleId.toString())
                .contentType(APPLICATION_JSON)
        ).andReturn().response

        then:
        1 * userService.setRole(userId, roleId)

        and:
        result.status == HttpStatus.OK.value()
    }
}