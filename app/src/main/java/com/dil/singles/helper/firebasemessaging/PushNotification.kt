package com.dil.singles.helper.firebasemessaging

data class PushNotification(
    var data:NotificationData,
    var to:String
)