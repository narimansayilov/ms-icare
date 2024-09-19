package com.icare.service

import com.icare.dto.criteria.UserCriteriaRequest
import com.icare.entity.UserEntity
import com.icare.repository.RoleRepository
import com.icare.repository.UserRepository
import com.icare.service.specification.UserSpecification
import io.github.benas.randombeans.EnhancedRandomBuilder
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import spock.lang.Specification

class UserServiceTest extends Specification {
    private AmazonS3Service amazonS3Service;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private UserService userService;
    private def random
    private def pageable
    private def specification

    void setup() {
        amazonS3Service = Mock()
        userRepository = Mock()
        roleRepository = Mock()
        specification = Mock(UserSpecification)
        pageable = Mock(Pageable)
        random = EnhancedRandomBuilder.aNewEnhancedRandomBuilder().build()
        userService = new UserService(amazonS3Service, userRepository, roleRepository)
   }

    def "GetUser"() {
        given:
        def id = 1L
        def userEntity = random.nextObject(UserEntity.class)

        when:
        def result = userService.getUser(id)

        then:
        1 * userRepository.findById(id) >> Optional.ofNullable(userEntity)

        and:
        result.id == userEntity.id
        result.name == userEntity.name
        result.surname == userEntity.surname
        result.birthDay == userEntity.birthDay
        result.email == userEntity.email
        result.phoneNumber == userEntity.phoneNumber
        result.photoUrl == userEntity.photoUrl
    }

    def "GetMyProfile"() {
        given:
        def email = "test@gmail.com"
        def userEntity = random.nextObject(UserEntity.class)

        userService.getCurrentEmail = { -> email }

        when:
        def result = userService.getMyProfile()

        then:
        1 * userRepository.findByEmail(email) >> Optional.ofNullable(userEntity)

        and:
        result.id == userEntity.id
        result.name == userEntity.name
        result.surname == userEntity.surname
        result.birthDay == userEntity.birthDay
        result.email == userEntity.email
        result.phoneNumber == userEntity.phoneNumber
        result.photoUrl == userEntity.photoUrl
    }

    def "GetAllUsers"() {
        given:
        def criteriaRequest = Mock(UserCriteriaRequest)
        List<UserEntity> userEntities = [
                random.nextObject(UserEntity),
                random.nextObject(UserEntity),
                random.nextObject(UserEntity)
        ]
        def userResponseList = UserMapper.INSTANCE.entitiesToResponses(userEntities)
        def page = Mock(Page)
        page.getContent() >> userEntities
        page.getTotalElements() >> userEntities.size()

        when:
        def result = userService.getAllUsers(pageable, criteriaRequest)

        then:
        1 * UserSpecification.getUserByCriteria(criteriaRequest) >> Mock(Specification)
        1 * userRepository.findAll(_, pageable) >> page

        and:
        result == userResponseList
    }
}
