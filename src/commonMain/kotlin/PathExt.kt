package org.ntqqrev.ktfs

import kotlinx.io.files.Path

operator fun Path.div(other: String): Path {
    return Path(this, other)
}

val String.asPath: Path
    get() = Path(this)
