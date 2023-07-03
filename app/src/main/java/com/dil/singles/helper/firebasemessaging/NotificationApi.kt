package com.dil.singles.helper.firebasemessaging

import com.dil.singles.helper.firebasemessaging.FirebaseConstants.Companion.CONTENT_TYPE
import com.dil.singles.helper.firebasemessaging.FirebaseConstants.Companion.SERVER_KEY
import okhttp3.ResponseBody
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.Response
import retrofit2.http.Body

interface NotificationApi {

    @Headers("Authorization: key=$SERVER_KEY","Content-type:$CONTENT_TYPE")
    @POST("fcm/send")
    suspend fun postNotification(
        @Body notification:PushNotification
    ): Response<ResponseBody>
}