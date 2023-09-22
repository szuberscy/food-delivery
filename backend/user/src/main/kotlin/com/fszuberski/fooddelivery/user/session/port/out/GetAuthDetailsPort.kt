package com.fszuberski.fooddelivery.user.session.port.out

import com.fszuberski.fooddelivery.user.session.core.domain.AuthDetails

interface GetAuthDetailsPort {
    fun getByUsername(username: String): AuthDetails?
}