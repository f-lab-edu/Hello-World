package me.soo.helloworld.model.notification

data class PushNotificationRequest(val targetId: String, val title: String, val message: String) {

    companion object {
        @JvmStatic
        fun create(targetId: String, title: String, message: String) = PushNotificationRequest(targetId, title, message)
    }
}
