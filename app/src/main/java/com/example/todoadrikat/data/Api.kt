package com.example.todoadrikat.data

import com.example.todoadrikat.list.Task
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserWebService {
    @GET("/sync/v9/user/")
    suspend fun fetchUser(): Response<User>
}

interface TasksWebService {
    @GET("/rest/v2/tasks/")
    suspend fun fetchTasks(): Response<List<Task>>
    @POST("/rest/v2/tasks/")
    suspend fun create(@Body task: Task): Response<Task>
    @POST("/rest/v2/tasks/{id}")
    suspend fun update(@Body task: Task, @Path("id") id: String = task.id): Response<Task>
    @DELETE("/rest/v2/tasks/{id}")
    suspend fun delete(@Body task: Task, @Path("id") id: String = task.id): Response<Unit>
}

object Api {
    private const val TOKEN = "274c69bdc44a4a182c5567a0c192b4e6df0e40a2"

    private val retrofit by lazy {
        // client HTTP
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor { chain ->
                // intercepteur qui ajoute le `header` d'authentification avec votre token:
                val newRequest = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $TOKEN")
                    .build()
                chain.proceed(newRequest)
            }
            .build()

        // transforme le JSON en objets kotlin et inversement
        val jsonSerializer = Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }

        // instance retrofit pour implémenter les webServices:
        Retrofit.Builder()
            .baseUrl("https://api.todoist.com/")
            .client(okHttpClient)
            .addConverterFactory(jsonSerializer.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    val userWebService : UserWebService by lazy {
        retrofit.create(UserWebService::class.java)
    }

    val tasksWebService : TasksWebService by lazy {
        retrofit.create(TasksWebService::class.java)
    }
}