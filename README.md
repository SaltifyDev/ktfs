# ktfs

ktfs is a lightweight wrapper around `kotlinx.io.files` that fixes non-ASCII path handling on Windows.

It wraps the file system implementation of `kotlinx-io` except for `mingwX64` target. Windows file system is re-implemented using `platform.windows` wide character APIs, which accepts **wide** characters, fixing the issue that paths with non-ASCII characters (for example, Chinese characters) cannot be accessed via the original implementation.

## Utility functions

```kotlin
inline fun withFs(fs: FileSystem = defaultFileSystem, block: FileSystem.() -> Unit) {
    block(fs)
}
```
