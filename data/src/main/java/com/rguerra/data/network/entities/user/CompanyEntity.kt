package com.rguerra.data.network.entities.user

import com.google.gson.annotations.SerializedName
import com.rguerra.domain.models.Company

data class CompanyEntity(
        @SerializedName("name")
        val name: String) {

    fun toDomain() = Company(companyName = name)
}