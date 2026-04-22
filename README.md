# ktfs

[![Maven Central](https://img.shields.io/maven-central/v/org.ntqqrev/ktfs)](https://central.sonatype.com/artifact/org.ntqqrev/ktfs/)

ktfs is a lightweight wrapper around `kotlinx.io.files` that fixes non-ASCII path handling on Windows.

It wraps the file system implementation of `kotlinx-io` except for `mingwX64` target. Windows file system is re-implemented using `platform.windows` wide character APIs, fixing the issue that paths with non-ASCII characters (for example, Chinese characters) cannot be accessed via the original implementation.

## Usage

### Composing a `Path`

`kotlinx.io.files.Path()` is the official way to create a path, which accepts vararg segments:

```kotlin
val path = Path("data", "notes", "today.txt")
```

`ktfs` adds a few small helpers for building paths more fluently:

```kotlin
operator fun Path.div(other: String): Path
val String.asPath: Path
```

These helpers are useful when you want to write path construction in a more DSL-like style. If you already have a `Path`, using `/ "child"` is the clearest option. If you start from a string literal, prefer `"root".asPath / "child"`.

```kotlin
import org.ntqqrev.ktfs.asPath
import org.ntqqrev.ktfs.div

val root = "data".asPath / "notes"
val today = root / "today.txt"
val archive = "data".asPath / "archive" / "today.txt"
```

### The `withFs` DSL

```kotlin
inline fun <R> withFs(fs: FileSystem = defaultFileSystem, block: FileSystem.() -> R): R
```

You can use `withFs` to execute a block of code with a specific file system implementation. By default, it uses the platform's default file system.

### Using `Path` as the main actor

`FileSystem` introduces a set of extension properties and functions on `Path` that allow you to perform file system operations directly on `Path` instances. This design promotes a more fluent and intuitive way to work with file paths.

```kotlin
interface FileSystem {
    val Path.exists: Boolean
    val Path.metadataOrNull: FileMetadata?
    val Path.isDirectory: Boolean
    val Path.isRegularFile: Boolean
    fun Path.createDirectories()
    fun Path.createDirectoriesOrFail()
    fun Path.delete()
    fun Path.deleteIfExists(): Boolean
    fun Path.moveTo(destination: Path)
    val Path.absolute: Path
    val Path.children: Collection<Path>

    fun Path.readText(): String
    fun Path.writeText(
        text: String,
        append: Boolean = false,
        createParentDirectories: Boolean = false,
    )
    fun Path.readBytes(): ByteArray
    fun Path.writeBytes(
        bytes: ByteArray,
        append: Boolean = false,
        createParentDirectories: Boolean = false,
    )
}
```

You can use these functions directly in `withFs` block, and they will be dispatched to the appropriate file system implementation based on the platform.

```kotlin
import kotlinx.io.files.Path
import org.ntqqrev.ktfs.asPath
import org.ntqqrev.ktfs.withFs

withFs {
    val note = "data".asPath / "notes" / "today.txt"
    if (note.exists && note.isRegularFile) {
        val archived = "data".asPath / "notes" / "today.done.txt" 
        note.moveTo(archived)
    }
    note.writeText("hello", createParentDirectories = true)
}
```
