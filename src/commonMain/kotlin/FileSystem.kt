package org.ntqqrev.ktfs

import kotlinx.io.*
import kotlinx.io.files.FileMetadata
import kotlinx.io.files.Path
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

interface FileSystem {
    fun exists(path: Path): Boolean
    fun delete(path: Path, mustExist: Boolean = true)
    fun createDirectories(path: Path, mustCreate: Boolean = false)
    fun atomicMove(source: Path, destination: Path)
    fun source(path: Path): RawSource
    fun sink(path: Path, append: Boolean = false): RawSink
    fun metadataOrNull(path: Path): FileMetadata?
    fun resolve(path: Path): Path
    fun list(directory: Path): Collection<Path>

    fun Path.readText(): String {
        source(this@readText).buffered().use {
            return it.readString()
        }
    }

    fun Path.write(text: String, append: Boolean = false) {
        sink(this@write, append).buffered().use {
            it.writeString(text)
        }
    }

    fun Path.readBytes(): ByteArray {
        source(this@readBytes).buffered().use {
            return it.readByteArray()
        }
    }

    fun Path.write(bytes: ByteArray, append: Boolean = false) {
        sink(this@write, append).buffered().use {
            it.write(bytes)
        }
    }
}

expect val defaultFileSystem: FileSystem

@OptIn(ExperimentalContracts::class)
inline fun <R> withFs(fs: FileSystem = defaultFileSystem, block: FileSystem.() -> R): R {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    return fs.block()
}