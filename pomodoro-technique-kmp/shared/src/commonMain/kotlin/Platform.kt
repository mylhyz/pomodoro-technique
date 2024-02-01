interface Platform {
    val name: String
    val isPortrait: Boolean
}

expect fun getPlatform(): Platform