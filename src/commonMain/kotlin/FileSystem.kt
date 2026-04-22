package org.ntqqrev.ktfs

import kotlinx.io.*
import kotlinx.io.files.FileMetadata
import kotlinx.io.files.Path
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.js.JsName

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

    val Path.exists: Boolean
        get() = this@FileSystem.exists(this)

    val Path.metadataOrNull: FileMetadata?
        get() = this@FileSystem.metadataOrNull(this)

    val Path.isDirectory: Boolean
        get() = metadataOrNull?.isDirectory == true

    val Path.isRegularFile: Boolean
        get() = metadataOrNull?.isRegularFile == true

    fun Path.createDirectories() {
        this@FileSystem.createDirectories(this@createDirectories)
    }

    fun Path.createDirectoriesOrFail() {
        this@FileSystem.createDirectories(this@createDirectoriesOrFail, mustCreate = true)
    }

    fun Path.delete() {
        this@FileSystem.delete(this@delete)
    }

    fun Path.deleteIfExists(): Boolean {
        if (!exists) {
            return false
        }
        delete()
        return true
    }

    fun Path.moveTo(destination: Path) {
        this@FileSystem.atomicMove(this@moveTo, destination)
    }

    val Path.absolute: Path
        get() = this@FileSystem.resolve(this)

    val Path.children: Collection<Path>
        get() = this@FileSystem.list(this)

    fun Path.readText(): String {
        source(this@readText).buffered().use {
            return it.readString()
        }
    }

    fun Path.writeText(
        text: String,
        append: Boolean = false,
        createParentDirectories: Boolean = false,
    ) {
        if (createParentDirectories) {
            parent?.createDirectories()
        }
        sink(this@writeText, append).buffered().use {
            it.writeString(text)
        }
    }

    fun Path.readBytes(): ByteArray {
        source(this@readBytes).buffered().use {
            return it.readByteArray()
        }
    }

    fun Path.writeBytes(
        bytes: ByteArray,
        append: Boolean = false,
        createParentDirectories: Boolean = false,
    ) {
        if (createParentDirectories) {
            parent?.createDirectories()
        }
        sink(this@writeBytes, append).buffered().use {
            it.write(bytes)
        }
    }

    @Deprecated("Use Path.writeText() instead")
    @JsName("legacyPathWriteText")
    fun Path.write(text: String, append: Boolean = false) {
        writeText(text, append)
    }

    @Deprecated("Use Path.writeBytes() instead")
    @JsName("legacyPathWriteBytes")
    fun Path.write(bytes: ByteArray, append: Boolean = false) {
        writeBytes(bytes, append)
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