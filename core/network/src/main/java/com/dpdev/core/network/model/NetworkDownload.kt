package com.dpdev.core.network.model

import java.io.InputStream

data class NetworkDownload(
    val stream: InputStream,
    val contentLength: Long
)
