package com.rguerra.data.network.entities.user

import com.google.gson.annotations.SerializedName
import com.rguerra.domain.models.User

data class UserEntity(
        @SerializedName("id")
        val userId: Int,
        @SerializedName("name")
        val repoName: String,
        @SerializedName("company")
        val company: CompanyEntity) {

    fun toDomain() = User(
            id = userId,
            name = repoName,
            company = company.toDomain())
}