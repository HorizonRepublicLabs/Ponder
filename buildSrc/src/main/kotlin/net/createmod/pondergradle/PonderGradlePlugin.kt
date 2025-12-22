package net.createmod.pondergradle

import dev.ithundxr.lotus.gradle.api.asm.LotusGradleASM
import net.createmod.pondergradle.nullability.PackageInfosExtension
import net.createmod.pondergradle.transformers.EnvironmentAnnotationTransformer
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply

class PonderGradlePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.extensions.create("defaultPackageInfos", PackageInfosExtension::class.java, project)

        // Register Transformers
        LotusGradleASM.addTransformer(EnvironmentAnnotationTransformer())

        // Apply lotus plugin
        project.apply(plugin = "dev.ithundxr.lotus.gradle")
    }
}
