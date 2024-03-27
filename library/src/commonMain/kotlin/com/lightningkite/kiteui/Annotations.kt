package com.lightningkite.kiteui

@Repeatable
@Target(AnnotationTarget.CLASS)
annotation class Routable(val path: String)

@Target(AnnotationTarget.CLASS)
annotation class FallbackRoute

@Target(AnnotationTarget.PROPERTY)
annotation class QueryParameter(val name: String = "")

@Target(AnnotationTarget.PROPERTY)
annotation class Hash
