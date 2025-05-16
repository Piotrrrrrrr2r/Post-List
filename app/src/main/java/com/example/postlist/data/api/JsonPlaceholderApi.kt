package com.example.postlist.data.api

import com.example.postlist.data.model.Post
import com.example.postlist.data.model.ToDo
import com.example.postlist.data.model.User
import retrofit2.http.GET
import retrofit2.http.Path

interface JsonPlaceholderApi {
    @GET("posts")
    suspend fun getPosts(): List<Post>

    @GET("posts/{postId}")
    suspend fun getPost(@Path("postId") postId: Int): Post

    @GET("users")
    suspend fun getUsers(): List<User>

    @GET("users/{userId}")
    suspend fun getUser(@Path("userId") userId: Int): User

    @GET("users/{userId}/todos")
    suspend fun getUserTodos(@Path("userId") userId: Int): List<ToDo>
}