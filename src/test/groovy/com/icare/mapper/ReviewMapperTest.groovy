package com.icare.mapper

import com.icare.dto.request.ReviewRequest
import com.icare.entity.ReviewEntity
import io.github.benas.randombeans.EnhancedRandomBuilder
import spock.lang.Specification

class ReviewMapperTest extends Specification {
    private def random

    void setup() {
        random = EnhancedRandomBuilder.aNewEnhancedRandomBuilder().build()
    }

    def "RequestToEntity"() {
        given:
        ReviewRequest reviewRequest = random.nextObject(ReviewRequest)
        Long userId = random.nextObject(Long)
        Long productId = random.nextObject(Long)

        when:
        def reviewEntity = ReviewMapper.INSTANCE.requestToEntity(reviewRequest, userId, productId)

        then:
        reviewEntity.comment == reviewRequest.comment
        reviewEntity.rating == reviewRequest.rating
        reviewEntity.user.id == userId
        reviewEntity.product.id == productId
        reviewEntity.rental.id == reviewRequest.rentalId
    }

    def "EntityToResponse"() {
        given:
        ReviewEntity reviewEntity = random.nextObject(ReviewEntity)

        when:
        def reviewResponse = ReviewMapper.INSTANCE.entityToResponse(reviewEntity)

        then:
        reviewResponse.id == reviewEntity.id
        reviewResponse.productId == reviewEntity.product.id
        reviewResponse.comment == reviewEntity.comment
        reviewResponse.rating == reviewEntity.rating
        reviewResponse.createdAt == reviewEntity.createdAt.toLocalDate()
        reviewResponse.user.id == reviewEntity.user.id
    }

    def "MapRequestToEntity"() {
        given:
        ReviewRequest reviewRequest = random.nextObject(ReviewRequest)
        ReviewEntity reviewEntity = new ReviewEntity()

        when:
        ReviewMapper.INSTANCE.mapRequestToEntity(reviewEntity, reviewRequest)

        then:
        reviewEntity.comment == reviewRequest.comment
        reviewEntity.rating == reviewRequest.rating
        reviewEntity.rental.id == reviewRequest.rentalId
    }
}
