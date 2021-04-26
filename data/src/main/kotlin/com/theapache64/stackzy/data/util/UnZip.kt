package com.theapache64.stackzy.data.util


import java.nio.file.Path
import java.util.zip.ZipFile
import kotlin.io.path.*

fun Path.unzip(
    outputDir: Path = getDefaultOutputDir(this)
): Path {
    // Delete existing first
    outputDir.toFile().deleteRecursively()

    ZipFile(this.toFile()).use { zip ->
        zip.entries().asSequence().forEach { entry ->
            if (!entry.isDirectory) {
                zip.getInputStream(entry).use { input ->
                    val outputFile = outputDir / entry.name

                    with(outputFile) {
                        if (!outputFile.parent.exists()) {
                            parent.createDirectories()
                        }
                    }

                    outputFile.createFile()
                    outputFile.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
            }
        }
    }

    return outputDir
}

private fun getDefaultOutputDir(inputZipPath: Path): Path {
    return inputZipPath.parent / inputZipPath.nameWithoutExtension
}
