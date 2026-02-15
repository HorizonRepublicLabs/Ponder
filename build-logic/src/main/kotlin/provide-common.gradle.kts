configurations {
    consumable("commonJava")
    consumable("commonResources")
}

val sourceSets = extensions.getByType<SourceSetContainer>()

artifacts {
    sourceSets["main"].run {
        java.sourceDirectories.forEach { add("commonJava", it) }
        resources.sourceDirectories.forEach { add("commonResources", it) }
    }
}
