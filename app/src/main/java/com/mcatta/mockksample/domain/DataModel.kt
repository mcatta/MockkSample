package com.mcatta.mockksample.domain

data class DataModel(val id: Int, val value: String) {
    override fun toString() = "DataModel with value: $value"
}