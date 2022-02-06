package ru.followy.util.followy_extensions.api

class APIConfig {
    companion object {
        private const val connectionType = "https"
        private const val domain = "developerslife.ru"
        const val url = "$connectionType://$domain/"
    }
}