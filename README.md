# ktfs

[![Maven Central](https://img.shields.io/maven-central/v/org.ntqqrev/ktfs)](https://central.sonatype.com/artifact/org.ntqqrev/ktfs/)

ktfs is a lightweight wrapper around `kotlinx.io.files` that fixes non-ASCII path handling on Windows.

It wraps the file system implementation of `kotlinx-io` except for `mingwX64` target. Windows file system is re-implemented using `platform.windows` wide character APIs, fixing the issue that paths with non-ASCII characters (for example, Chinese characters) cannot be accessed via the original implementation.

## Utility functions

### The `withFs` DSL

```kotlin
inline fun withFs(fs: FileSystem = defaultFileSystem, block: FileSystem.() -> Unit) {
    block(fs)
}
```

You can use `withFs` to execute a block of code with a specific file system implementation. By default, it uses the platform's default file system.

### Extension functions for `kotlinx.io.files.Path`

```kotlin
interface FileSystem {
    fun Path.readText()
    fun Path.write(text: String, append: Boolean = false)
    fun Path.readBytes(): ByteArray
    fun Path.write(bytes: ByteArray, append: Boolean = false)
}
```

You can use these functions directly in `withFs` block, and they will be dispatched to the appropriate file system implementation based on the platform.
