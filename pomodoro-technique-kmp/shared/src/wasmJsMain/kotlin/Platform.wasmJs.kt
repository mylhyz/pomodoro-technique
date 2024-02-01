class WasmPlatform: Platform {
    override val name: String = "Web with Kotlin/Wasm"
    override val isPortrait: Boolean = false
}

actual fun getPlatform(): Platform = WasmPlatform()