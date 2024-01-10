/*
 * Copyright 2024 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.gradle.generator.resources.file

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.TypeSpec
import dev.icerock.gradle.generator.Constants
import dev.icerock.gradle.generator.PlatformResourceGenerator
import dev.icerock.gradle.generator.addJvmResourcesClassLoaderProperty
import dev.icerock.gradle.metadata.resource.FileMetadata
import java.io.File

internal class JvmFileResourceGenerator(
    private val className: String,
    private val resourcesGenerationDir: File
) : PlatformResourceGenerator<FileMetadata> {
    override fun imports(): List<ClassName> = emptyList()

    override fun generateInitializer(metadata: FileMetadata): CodeBlock {
        return CodeBlock.of(
            "FileResource(resourcesClassLoader = %L, filePath = %S)",
            Constants.Jvm.resourcesClassLoaderPropertyName,
            "$FILES_DIR/${metadata.filePath.name}"
        )
    }

    override fun generateResourceFiles(data: List<FileMetadata>) {
        val fontsDir = File(resourcesGenerationDir, FILES_DIR)
        fontsDir.mkdirs()

        data.map { it.filePath }.forEach { file ->
            file.copyTo(File(fontsDir, file.name))
        }
    }

    override fun generateBeforeProperties(
        builder: TypeSpec.Builder,
        metadata: List<FileMetadata>
    ) {
        builder.addJvmResourcesClassLoaderProperty(className)
    }

    private companion object {
        const val FILES_DIR = "files"
    }
}
