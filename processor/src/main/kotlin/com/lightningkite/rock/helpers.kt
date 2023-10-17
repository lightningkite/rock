package com.lightningkite.rock

import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSAnnotation


fun KSAnnotated.annotation(name: String, packageName: String = "com.lightningkite.rock"): KSAnnotation? {
    return this.annotations.find {
        it.shortName.getShortName() == name &&
                it.annotationType.resolve().declaration.qualifiedName?.asString() == "$packageName.$name"
    }
}
fun KSAnnotated.annotations(name: String, packageName: String = "com.lightningkite.rock"): List<KSAnnotation> {
    return this.annotations.filter {
        it.shortName.getShortName() == name &&
                it.annotationType.resolve().declaration.qualifiedName?.asString() == "$packageName.$name"
    }.toList()
}